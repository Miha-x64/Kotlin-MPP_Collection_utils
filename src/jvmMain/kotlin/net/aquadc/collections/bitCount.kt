package net.aquadc.collections

@PublishedApi
internal actual val Long.bitCount: Int
    get() = java.lang.Long.bitCount(this)
