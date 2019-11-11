package net.aquadc.collections

@PublishedApi
internal actual val Long.bitCount: Int
    get() {
        var i = this
        i = i - (i ushr 1 and 0x5555555555555555L)
        i = (i and 0x3333333333333333L) + (i ushr 2 and 0x3333333333333333L)
        i = i + (i ushr 4) and 0x0f0f0f0f0f0f0f0fL
        i = i + (i ushr 8)
        i = i + (i ushr 16)
        i = i + (i ushr 32)
        return i.toInt() and 0x7f
    }
