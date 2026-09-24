package com.graphsync

sealed class SyncOp<T> {
    data class AddNode<T>(val node: T) : SyncOp<T>()
    data class RemoveNode<T>(val node: T) : SyncOp<T>()
    data class AddEdge<T>(val from: T, val to: T) : SyncOp<T>()
    data class RemoveEdge<T>(val from: T, val to: T) : SyncOp<T>()
}