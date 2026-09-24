package com.graphsync

object GraphDiff {
    fun <T> calculateDiff(source: Graph<T>, target: Graph<T>): List<SyncOp<T>> {
        val ops = mutableListOf<SyncOp<T>>()

        // Nodes to remove (in target but not in source)
        target.nodes.filter { it !in source.nodes }.forEach {
            ops.add(SyncOp.RemoveNode(it))
        }

        // Nodes to add (in source but not in target)
        source.nodes.filter { it !in target.nodes }.forEach {
            ops.add(SyncOp.AddNode(it))
        }

        // Edges to remove (in target but not in source)
        target.edges.filter { it !in source.edges }.forEach {
            ops.add(SyncOp.RemoveEdge(it.first, it.second))
        }

        // Edges to add (in source but not in target)
        source.edges.filter { it !in target.edges }.forEach {
            ops.add(SyncOp.AddEdge(it.first, it.second))
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