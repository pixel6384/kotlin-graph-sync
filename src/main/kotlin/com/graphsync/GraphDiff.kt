package com.graphsync

object GraphDiff {
    fun <T> calculateDiff(source: Graph<T>, target: Graph<T>): List<SyncOp<T>> {
        val ops = mutableListOf<SyncOp<T>>()

        // 1. Nodes to add (Must happen first so edges can reference them)
        source.nodes.filter { it !in target.nodes }.forEach {
            ops.add(SyncOp.AddNode(it))
        }

        // 2. Edges to add
        source.edges.filter { it !in target.edges }.forEach {
            ops.add(SyncOp.AddEdge(it.first, it.second))
        }

        // 3. Edges to remove (Must happen before removing nodes they connect to)
        target.edges.filter { it !in source.edges }.forEach {
            ops.add(SyncOp.RemoveEdge(it.first, it.second))
        }

        // 4. Nodes to remove
        target.nodes.filter { it !in source.nodes }.forEach {
            ops.add(SyncOp.RemoveNode(it))
        }

        return ops
    }

    fun <T> applyDiff(target: Graph<T>, ops: List<SyncOp<T>>): Graph<T> {
        var currentNodes = target.nodes.toMutableSet()
        var currentEdges = target.edges.toMutableSet()

        ops.forEach {
            when (it) {
                is SyncOp.AddNode -> currentNodes.add(it.node)
                is SyncOp.RemoveNode -> {
                    currentNodes.remove(it.node)
                    currentEdges.removeIf { edge -> edge.first == it.node || edge.second == it.node }
                }
                is SyncOp.AddEdge -> currentEdges.add(it.from to it.to)
                is SyncOp.RemoveEdge -> currentEdges.remove(it.from to it.to)
            }
        }

        return Graph(currentNodes, currentEdges)
    }
}