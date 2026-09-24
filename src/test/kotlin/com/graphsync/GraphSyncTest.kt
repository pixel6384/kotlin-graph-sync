package com.graphsync

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GraphSyncTest {
    @Test
    fun testGraphSynchronization() {
        val source = Graph(
            nodes = setOf(1, 2, 3),
            edges = setOf(1 to 2, 2 to 3)
        )
        val target = Graph(
            nodes = setOf(1, 4),
            edges = setOf(1 to 4)
        )

        val diff = GraphDiff.calculateDiff(source, target)
        val synced = GraphDiff.applyDiff(target, diff)

        assertEquals(source.nodes, synced.nodes)
        assertEquals(source.edges, synced.edges)
    }

    @Test
    fun testIdentitySync() {
        val source = Graph(setOf(1, 2), setOf(1 to 2))
        val target = Graph(setOf(1, 2), setOf(1 to 2))

        val diff = GraphDiff.calculateDiff(source, target)
        assertEquals(0, diff.size, "Diff should be empty for identical graphs")
        
        val synced = GraphDiff.applyDiff(target, diff)
        assertEquals(source, synced)
    }

    @Test
    fun testEmptyToPopulated() {
        val source = Graph(setOf(1), emptySet())
        val target = Graph(emptySet(), emptySet())

        val diff = GraphDiff.calculateDiff(source, target)
        val synced = GraphDiff.applyDiff(target, diff)

        assertEquals(source, synced)
    }

    @Test
    fun testPopulatedToEmpty() {
        val source = Graph(emptySet(), emptySet())
        val target = Graph(setOf(1, 2), setOf(1 to 2))

        val diff = GraphDiff.calculateDiff(source, target)
        val synced = GraphDiff.applyDiff(target, diff)

        assertEquals(source, synced)
    }

    @Test
    fun testInvalidEdgeOperation() {
        val target = Graph(setOf(1), emptySet())
        val ops = listOf(SyncOp.AddEdge(1, 2))

        assertFailsWith<IllegalArgumentException> {
            GraphDiff.applyDiff(target, ops)
        }
    }

    @Test
    fun testNodeRemovalCleansUpEdges() {
        val source = Graph(setOf(1), emptySet())
        val target = Graph(setOf(1, 2), setOf(1 to 2))

        val diff = GraphDiff.calculateDiff(source, target)
        val synced = GraphDiff.applyDiff(target, diff)

        assertEquals(source.nodes, synced.nodes)
        assertEquals(source.edges, synced.edges)
    }
}