package com.graphsync

import kotlin.test.Test
import kotlin.test.assertEquals

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
}