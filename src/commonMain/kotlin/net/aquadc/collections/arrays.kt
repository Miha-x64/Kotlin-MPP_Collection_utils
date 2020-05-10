@file:JvmName("Arrayz")
package net.aquadc.collections

import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

/**
 * Returns a view of the portion of this array
 * between the specified [fromIndex] (inclusive) and [toIndex] (exclusive).
 * The returned list is backed by this array,
 * so changes in this array are reflected in the returned list.
 */
fun <E> Array<out E>.subList(fromIndex: Int, toIndex: Int): List<E> {
    subArrayRangeCheck(fromIndex, toIndex, size)
    return newSubList(fromIndex, toIndex, 1)
}

/**
 * Returns a view of the portion of this array
 * between the specified [fromIndex] (inclusive) and [toIndex] (exclusive)
 * and with the given [step].
 * The returned list is backed by this array,
 * so changes in this array are reflected in the returned list.
 * Example: [0, 1, 2, 3, 4, 5, 6, 7, 8].subList(1, 8, 3) => [1, 4, 7]
 */
fun <E> Array<out E>.subList(fromIndex: Int, toIndex: Int, step: Int): List<E> {
    subArrayRangeCheck(fromIndex, toIndex, size)
    if (step < 1) throw IllegalArgumentException("step($step) < 1")
    val range = toIndex - fromIndex
    val size = range / step
    val tail = range % step
    // [0, 1, 2, 3, 4, 5, 6, 7, 8].subList(1, 8, 3), range=8-1=7, 7/3=2, 7%3=1, notionalRange=9, [1, 10):
    //  0,[1, _, _, 4, _, _, 7, _, _)
    return newSubList(
            fromIndex = fromIndex,
            toIndex = if (tail == 0) toIndex else (fromIndex + (size+1) * step),
            step = step
    )
}

private class SubArray<E>( // invariant <E> does not require @UnsafeVariance; no difference when cast to List
        private val array: Array<E>,
        private val fromIndex: Int,
        private val toIndex: Int,
        private val step: Int
) : AbstractList<E>() { // derive isEmpty, toString, equals, hashCode

    override val size: Int
        get() = (toIndex - fromIndex) / step // we've ensured `(toIndex - fromIndex) % step == 0` earlier

    override fun get(index: Int): E {
        rangeCheck(index, size)
        return array[fromIndex + (index * step)]
    }

    // iteratorless and non-virtual
    override fun contains(element: E): Boolean =
        indexOf(element) >= 0
    override fun containsAll(elements: Collection<E>): Boolean =
        elements.all { indexOf(it) >= 0 }

    // direct array access from iterator, no [this] capturing & dereferencing
    override fun iterator(): Iterator<E> =
            ArrayIterator(array, fromIndex, toIndex, step)
    override fun listIterator(): ListIterator<E> =
            ListArrayIterator(array, fromIndex, fromIndex, toIndex, step)
    override fun listIterator(index: Int): ListIterator<E> {
        listIteratorBoundsCheck(index, size)
        return ListArrayIterator(array, fromIndex, fromIndex + (index * step), toIndex, step)
    }

    // direct array access
    override fun indexOf(element: E): Int {
        var index = fromIndex
        while (index < toIndex) {
            if (array[index] == element)
                return (index - fromIndex) / step

            index += step
        }
        return -1
    }
    override fun lastIndexOf(element: E): Int {
        var index = toIndex - step // index of last
        while (index >= fromIndex) {
            if (array[index] == element)
                return (index - fromIndex) / step

            index -= step
        }
        return -1
    }

    // self-reproduction
    override fun subList(fromIndex: Int, toIndex: Int): List<E> {
        subArrayRangeCheck(fromIndex, toIndex, size)
        val from = this.fromIndex
        return array.newSubList(from + (fromIndex * step), from + (toIndex * step), step)
    }

    // non-virtual direct array access
    override fun toArray(): Array<Any?> =
        copyInto(arrayOfNulls<Any>(size))

    override fun <T> toArray(array: Array<T>): Array<T> =
        copyInto(if (array.size < size) array.copyOf(size) else array)
     // The only way to copy an array   ^^^^^^^^^^^^^^^^^^ in MPP saving its runtime type
     // also involves copying its useless contents. :'(
     // Do you know another way (without expect-actual ;)?

    @Suppress("UNCHECKED_CAST") // as designed, we ignore <T> and put elements forcibly
    private fun <E> copyInto(dst: Array<out Any?>): Array<E> {
        dst as Array<Any?>

        if (step == 1) array.copyInto(dst, 0, fromIndex, toIndex) // common case, fast path
        else { // okay_face.jpg
            var srcIdx = fromIndex
            var dstIdx = 0
            while (srcIdx < toIndex) {
                dst[dstIdx++] = array[srcIdx]
                srcIdx += step
            }
        }

        // ignored by toArray(), required for toArray<T>(array)
        if (dst.size > size) dst[size] = null

        return dst as Array<E>
    }
}

// default iterator captures List and calls virtual get(); we capture array and use direct []
internal open class ArrayIterator<out E>(
        protected val array: Array<out E>,
        protected var index: Int,
        private val toIndex: Int,
        protected val step: Int
) : Iterator<E> {
    override fun hasNext(): Boolean = index < toIndex
    override fun next(): E {
        if (index == toIndex) throw NoSuchElementException()
        val next = array[index]
        index += step
        return next
    }
}
internal class ListArrayIterator<out E>(
        array: Array<out E>,
        private val fromIndex: Int,
        index: Int,
        toIndex: Int,
        step: Int
) : ArrayIterator<E>(array, index, toIndex, step), ListIterator<E> {
    override fun hasPrevious(): Boolean = index > fromIndex
    override fun nextIndex(): Int = (index - fromIndex) / step
    override fun previous(): E {
        if (index == fromIndex) throw NoSuchElementException()
        index -= step
        return array[index]
    }
    override fun previousIndex(): Int = (index - fromIndex) / step - 1
}

// utils

@JvmSynthetic internal fun subArrayRangeCheck(fromIndex: Int, toIndex: Int, size: Int) {
    if (fromIndex < 0) throw IndexOutOfBoundsException("fromIndex($fromIndex) < 0")
    if (toIndex > size) throw IndexOutOfBoundsException("toIndex($toIndex) > size($size)")
    if (fromIndex > toIndex) throw IllegalArgumentException("fromIndex($fromIndex) > toIndex($toIndex)")
}

@JvmSynthetic internal fun <E> Array<out E>.newSubList(fromIndex: Int, toIndex: Int, step: Int): List<E> =
        if (fromIndex == toIndex) emptyList()
        else SubArray(this, fromIndex, toIndex, step)

@JvmSynthetic internal fun rangeCheck(index: Int, size: Int) {
    if (index < 0 || index >= size)
        throw IndexOutOfBoundsException("Index: $index, Size: $size")
}

@JvmSynthetic internal fun listIteratorBoundsCheck(index: Int, size: Int) {
    if (index < 0 || index > size) throw IndexOutOfBoundsException("index: $index, size: $size")
}
