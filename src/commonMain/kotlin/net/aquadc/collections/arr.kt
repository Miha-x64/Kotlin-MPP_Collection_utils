@file:Suppress("NOTHING_TO_INLINE", "DEPRECATION", "UNCHECKED_CAST")
package net.aquadc.collections

import kotlin.jvm.JvmField
import kotlin.jvm.JvmSynthetic

/**
 * Re-implementation of Arrays.asList as an inline class.
 * It is more lightweight than List because there's no additional wrapper,
 * and greater than Array because does not require reified type parameters.
 */
@Suppress("OVERRIDE_BY_INLINE", "RESERVED_MEMBER_INSIDE_INLINE_CLASS")
inline class Arr<E> // yep, it's invariant, because has set() method, similarly to Arrays.asList
@Deprecated("private") constructor(
    @PublishedApi @JvmSynthetic @JvmField internal val array: Array<out Any?>
) : List<E>, RandomAccess {

    override inline val size: Int
        get() = array.size

    override inline fun isEmpty(): Boolean =
        array.isEmpty()

    override inline fun contains(element: E): Boolean =
        array.contains(element)

    override fun iterator(): Iterator<E> =
        if (array.isEmpty()) emptyList<E>().iterator() // singleton
        else array.iterator() as Iterator<E>

    override fun containsAll(elements: Collection<E>): Boolean =
        elements.all { array.contains(it) }

    override inline fun get(index: Int): E =
        array[index] as E // yep, this would throw AIOOBE instead of IOOBE

    override inline fun indexOf(element: E): Int =
        array.indexOf(element)

    override inline fun lastIndexOf(element: E): Int =
        array.lastIndexOf(element)

    override fun listIterator(): ListIterator<E> =
        if (array.isEmpty()) emptyList<E>().listIterator()
        else array.asList().listIterator() as ListIterator<E>

    override fun listIterator(index: Int): ListIterator<E> =
        if (array.isEmpty()) emptyList<E>().listIterator(index)
        else array.asList().listIterator(index) as ListIterator<E>

    override fun subList(fromIndex: Int, toIndex: Int): List<E> =
        if (array.isEmpty()) emptyList<E>().subList(fromIndex, toIndex)
        else array.asList().subList(fromIndex, toIndex) as List<E>

    @Deprecated("hiding reserved override as a precaution", level = DeprecationLevel.HIDDEN)
    override fun equals(other: Any?): Boolean =
        other is List<*> && eq(other)

    fun eq(other: List<*>): Boolean {
        if (other is Arr<*>)
            return array.contentEquals(other.array)

        val iter = other.listIterator()
        for (i in array.indices) {
            if (!iter.hasNext() || array[i] != iter.next()) return false
        }
        return !iter.hasNext()
    }

    @Deprecated("hiding reserved override as a precaution", level = DeprecationLevel.HIDDEN)
    override fun hashCode(): Int =
        array.contentHashCode()

    override inline fun toString(): String =
        array.contentToString()

    // array-style setter
    inline operator fun set(index: Int, value: E) {
        (array as Array<E>)[index] = value
        // in Array<T>, T may be either Any or E, both should be safe
    }

    companion object {
        val Empty: Arr<Nothing> = Arr(emptyArray())
    }

}

inline fun <E> emptyArr(): Arr<E> =
    Arr.Empty as Arr<E>

inline fun <E> arrOf(): Arr<E> =
    Arr.Empty as Arr<E>

inline fun <E> arrOf(vararg values: E): Arr<E> =
    Arr(values)

inline fun <E> arrOfNulls(size: Int): Arr<E?> =
    Arr(arrayOfNulls(size))

inline fun <E> Arr(size: Int, init: (Int) -> E): Arr<E> =
    Arr(arrayOfNulls<Any>(size).also {
        for (i in 0 until size) it[i] = init(i)
    })


inline fun <E, R> Arr<out E>.map(transform: (E) -> R): Arr<R> =
    Arr(Array<Any?>(size) { transform(array[it] as E) })

inline fun <E> Arr<out E>.map(transform: (E) -> Int): IntArray =
    IntArray(size) { transform(array[it] as E) }

inline fun <E> Arr<out E>.map(transform: (E) -> Long): LongArray =
    LongArray(size) { transform(array[it] as E) }

inline fun <E> Arr<out E>.map(transform: (E) -> Float): FloatArray =
    FloatArray(size) { transform(array[it] as E) }

inline fun <E> Arr<out E>.map(transform: (E) -> Double): DoubleArray =
    DoubleArray(size) { transform(array[it] as E) }


inline fun <E, R> Arr<out E>.mapIndexed(transform: (index: Int, E) -> R): Arr<R> =
    Arr(Array<Any?>(size) { transform(it, array[it] as E) })

inline fun <E> Arr<out E>.mapIndexed(transform: (index: Int, E) -> Int): IntArray =
    IntArray(size) { transform(it, array[it] as E) }

inline fun <E> Arr<out E>.mapIndexed(transform: (index: Int, E) -> Long): LongArray =
    LongArray(size) { transform(it, array[it] as E) }

inline fun <E> Arr<out E>.mapIndexed(transform: (index: Int, E) -> Float): FloatArray =
    FloatArray(size) { transform(it, array[it] as E) }

inline fun <E> Arr<out E>.mapIndexed(transform: (index: Int, E) -> Double): DoubleArray =
    DoubleArray(size) { transform(it, array[it] as E) }
