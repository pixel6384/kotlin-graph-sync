package com.graphsync

data class Graph<T>(
    val nodes: Set<T> = emptySet(),
    val edges: Set<Pair<T, T>> = emptySet()
)
