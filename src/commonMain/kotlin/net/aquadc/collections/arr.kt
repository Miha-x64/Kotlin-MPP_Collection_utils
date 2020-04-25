@file:Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")
@file:JvmName("Arrs")
package net.aquadc.collections

import kotlin.jvm.JvmField
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

/**
 * Re-implementation of Arrays.asList as an inline class.
 * It is more lightweight than List because there's no additional wrapper,
 * and greater than Array because does not require reified type parameters.
 */
@Suppress("OVERRIDE_BY_INLINE", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
inline class Arr<E> // yep, it's invariant, because has set() method, similarly to Arrays.asList
@PublishedApi internal constructor(
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
        else ListArrayIterator(array, 0, 0, array.size) as ListIterator<E>

    override fun listIterator(index: Int): ListIterator<E> =
        if (array.isEmpty()) emptyList<E>().listIterator(index)
        else {
            val sz = array.size
            listIteratorBoundsCheck(index, sz)
            ListArrayIterator(array, index, index, sz) as ListIterator<E>
        }

    override fun subList(fromIndex: Int, toIndex: Int): List<E> =
        if (array.isEmpty()) emptyList<E>().subList(fromIndex, toIndex)
        else array.subList(fromIndex, toIndex) as List<E>

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
    inline operator fun set(index: Int, value: E): E {
        val old = array[index]
        (array as Array<E>)[index] = value
        // in Array<T>, T may be either Any or E, both should be safe

        return old as E
    }

    fun copy(fromIndex: Int = 0, toIndex: Int = array.size): Arr<E> =
        if (fromIndex == toIndex) {
            check(fromIndex >= 0)
            check(toIndex <= array.size)
            Empty as Arr<E>
        } else {
            Arr(array.copyOfRange(fromIndex, toIndex))
        }

    // transforms. Unfortunately, they could not be added as extensions because they would compete with Kotlin's ones.
    // By the way, kotlinc seem not to generate instance box methods for inline non-overrides

    inline fun <R> map(transform: (E) -> R): Arr<R> =
        Arr(Array<Any?>(size) { transform(array[it] as E) })

    inline fun mapToInt(transform: (E) -> Int): IntArray =
        IntArray(size) { transform(array[it] as E) }

    inline fun mapToLong(transform: (E) -> Long): LongArray =
        LongArray(size) { transform(array[it] as E) }

    inline fun mapToFloat(transform: (E) -> Float): FloatArray =
        FloatArray(size) { transform(array[it] as E) }

    inline fun mapToDouble(transform: (E) -> Double): DoubleArray =
        DoubleArray(size) { transform(array[it] as E) }


    inline fun <R> mapIndexed(transform: (index: Int, E) -> R): Arr<R> =
        Arr(Array<Any?>(size) { transform(it, array[it] as E) })

    inline fun mapIndexedToInt(transform: (index: Int, E) -> Int): IntArray =
        IntArray(size) { transform(it, array[it] as E) }

    inline fun mapIndexedToLong(transform: (index: Int, E) -> Long): LongArray =
        LongArray(size) { transform(it, array[it] as E) }

    inline fun mapIndexedToFloat(transform: (index: Int, E) -> Float): FloatArray =
        FloatArray(size) { transform(it, array[it] as E) }

    inline fun mapIndexedToDouble(transform: (index: Int, E) -> Double): DoubleArray =
        DoubleArray(size) { transform(it, array[it] as E) }

    // constants

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

