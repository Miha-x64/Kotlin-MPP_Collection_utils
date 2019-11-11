@file:Suppress("NOTHING_TO_INLINE", "DEPRECATION")
package net.aquadc.collections

import kotlin.jvm.JvmField
import kotlin.jvm.JvmSynthetic

/**
 * Immutable inline EnumSet implementation.
 */
inline class InlineEnumSet<E : Enum<E>>
@Deprecated("private") constructor(
    @PublishedApi @JvmSynthetic @JvmField internal val set: Long
)

// factories

inline fun <E : Enum<E>> noneOf(): InlineEnumSet<E> =
    InlineEnumSet(0)

inline fun <reified E : Enum<E>> allOf(): InlineEnumSet<E> =
    InlineEnumSet(-1L ushr (64 - enumValues<E>().size))

inline fun <E : Enum<E>> E.asSet(): InlineEnumSet<E> =
    InlineEnumSet(1L shl this.ord)

inline operator fun <E : Enum<E>> E.plus(other: E): InlineEnumSet<E> =
    InlineEnumSet((1L shl this.ord) or (1L shl other.ord))

inline operator fun <E : Enum<E>> InlineEnumSet<E>.plus(other: E): InlineEnumSet<E> =
    InlineEnumSet(this.set or (1L shl other.ord))

inline operator fun <E : Enum<E>> E.plus(other: InlineEnumSet<E>): InlineEnumSet<E> =
    InlineEnumSet((1L shl this.ord) or other.set)

inline operator fun <E : Enum<E>> InlineEnumSet<E>.plus(other: InlineEnumSet<E>): InlineEnumSet<E> =
    InlineEnumSet(this.set or other.set)

inline infix fun <E : Enum<E>> InlineEnumSet<E>.intersect(other: InlineEnumSet<E>): InlineEnumSet<E> =
    InlineEnumSet(this.set and other.set)

inline infix fun <E : Enum<E>> InlineEnumSet<E>.subtract(other: InlineEnumSet<E>): InlineEnumSet<E> =
    InlineEnumSet(this.set and other.set.inv())

inline infix fun <E : Enum<E>> InlineEnumSet<E>.union(other: InlineEnumSet<E>): InlineEnumSet<E> =
    InlineEnumSet(this.set or other.set)

// query

inline val <E : Enum<E>> InlineEnumSet<E>.size: Int
    get() = set.bitCount

inline val <E : Enum<E>> InlineEnumSet<E>.isEmpty: Boolean
    get() = set == 0L

inline operator fun <E : Enum<E>> InlineEnumSet<E>.contains(element: E): Boolean =
    (set and (1L shl element.ord)) != 0L

inline fun <E : Enum<E>> InlineEnumSet<E>.containsAll(elements: InlineEnumSet<E>): Boolean =
    (set and elements.set) == elements.set

inline fun <reified E : Enum<E>, R> InlineEnumSet<E>.map(transform: (E) -> R): Arr<R> {
    val outArr = arrOfNulls<R>(size)
    if (size != 0) { // forEachIndexed:
        val values = enumValues<E>()
        var idx = 0
        var ord = 0
        var mask = set
        while (mask != 0L) {
            if ((mask and 1L) == 1L) {
                outArr[idx++] = transform(values[ord])
            }
            mask = mask ushr 1
            ord++
        }
    }
    @Suppress("UNCHECKED_CAST")
    return outArr as Arr<R>
}

inline fun <reified E : Enum<E>> InlineEnumSet<E>.toArr(): Arr<E> =
    map { it }

inline infix fun <E : Enum<E>> InlineEnumSet<E>.eq(other: InlineEnumSet<E>): Boolean =
    set == other.set

// util

@PublishedApi internal val Enum<*>.ord: Int
    get() = ordinal.also { require(it < 64) { "enum type ${this::class} must have no more than 64 constants" } }

@PublishedApi internal expect val Long.bitCount: Int
