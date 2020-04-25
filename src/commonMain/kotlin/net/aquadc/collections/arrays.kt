@file:JvmName("Arrayz")
package net.aquadc.collections

import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

/**
 * Returns a view of the portion of this array
 * between the specified [fromIndex] (inclusive) and [toIndex] (exclusive).
 * The returned list is backed by this array,
 * so changes in the returned list are reflected in this array, and vice-versa.
 */
fun <E> Array<out E>.subList(fromIndex: Int, toIndex: Int): List<E> {
    subArrayRangeCheck(fromIndex, toIndex, size)
    return newSubList(fromIndex, toIndex)
}

private class SubArray<E>( // invariant <E> does not require @UnsafeVariance; no difference when cast to List
        private val array: Array<E>,
        private val fromIndex: Int,
        private val toIndex: Int
) : AbstractList<E>() { // derive isEmpty, toString, equals, hashCode

    override val size: Int
        get() = toIndex - fromIndex

    override fun get(index: Int): E {
        val from = fromIndex
        rangeCheck(index, toIndex - from)
        return array[from + index]
    }

    override fun contains(element: E): Boolean =
        indexOf(element) >= 0
    override fun containsAll(elements: Collection<E>): Boolean =
        elements.all { indexOf(it) >= 0 }

    // toArray implementations cannot be reliably tested in MPP
    /*override fun toArray(): Array<Any?> {
        val dest = arrayOfNulls<Any>(size)
        array.copyInto(dest, 0, fromIndex, toIndex)
        return dest
    }
    @Suppress("UNCHECKED_CAST")
    override fun <T> toArray(array: Array<T>): Array<T> {
        val mySize = size
        if (array.size < mySize) return super.toArray(array) // hello `toArray(new Crap[0])` my old reflective friend

        this.array.copyInto(array as Array<E>, 0, fromIndex, toIndex)
        if (array.size > mySize) (array as Array<Any?>)[mySize] = null
        return array
    }*/

    override fun iterator(): Iterator<E> =
            ArrayIterator(array, fromIndex, toIndex)
    override fun listIterator(): ListIterator<E> =
            ListArrayIterator(array, fromIndex, fromIndex, toIndex)
    override fun listIterator(index: Int): ListIterator<E> {
        listIteratorBoundsCheck(index, size)
        return ListArrayIterator(array, fromIndex, fromIndex + index, toIndex)
    }

    // direct array access
    override fun indexOf(element: E): Int {
        val from = fromIndex
        for (index in from until toIndex)
            if (array[index] == element)
                return index - from
        return -1
    }
    override fun lastIndexOf(element: E): Int {
        val from = fromIndex
        for (index in (toIndex-1) downTo from)
            if (array[index] == element)
                return index - from
        return -1
    }

    // self-reproduction
    override fun subList(fromIndex: Int, toIndex: Int): List<E> {
        subArrayRangeCheck(fromIndex, toIndex, size)
        val from = this.fromIndex
        return array.newSubList(from + fromIndex, from + toIndex)
    }
}

// default iterator captures List and calls virtual get(); we capture array and use direct []
internal open class ArrayIterator<out E>(
        protected val array: Array<out E>,
        protected var index: Int,
        private val toIndex: Int
) : Iterator<E> {
    override fun hasNext(): Boolean = index < toIndex
    override fun next(): E {
        if (index == toIndex) throw NoSuchElementException()
        return array[index++]
    }
}
internal class ListArrayIterator<out E>(
        array: Array<out E>,
        private val fromIndex: Int,
        index: Int,
        toIndex: Int
) : ArrayIterator<E>(array, index, toIndex), ListIterator<E> {
    override fun hasPrevious(): Boolean = index > fromIndex
    override fun nextIndex(): Int = index - fromIndex
    override fun previous(): E {
        if (index == fromIndex) throw NoSuchElementException()
        return array[--index]
    }
    override fun previousIndex(): Int = index - fromIndex - 1
}

// utils

@JvmSynthetic internal fun subArrayRangeCheck(fromIndex: Int, toIndex: Int, size: Int) {
    if (fromIndex < 0) throw IndexOutOfBoundsException("fromIndex($fromIndex) < 0")
    if (toIndex > size) throw IndexOutOfBoundsException("toIndex($toIndex) > size($size)")
    if (fromIndex > toIndex) throw IllegalArgumentException("fromIndex($fromIndex) > toIndex($toIndex)")
}

@JvmSynthetic internal fun <E> Array<out E>.newSubList(fromIndex: Int, toIndex: Int): List<E> =
        if (fromIndex == toIndex) emptyList()
        else SubArray(this, fromIndex, toIndex)

@JvmSynthetic internal fun rangeCheck(index: Int, size: Int) {
    if (index < 0 || index >= size)
        throw IndexOutOfBoundsException("Index: $index, Size: $size")
}

@JvmSynthetic internal fun listIteratorBoundsCheck(index: Int, size: Int) {
    if (index < 0 || index > size) throw IndexOutOfBoundsException("index: $index, size: $size")
}
