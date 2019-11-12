package net.aquadc.collections

@PublishedApi
internal actual inline val Long.bitCount: Int
    get() = java.lang.Long.bitCount(this)
