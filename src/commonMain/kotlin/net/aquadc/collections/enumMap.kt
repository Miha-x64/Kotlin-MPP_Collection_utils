@file:Suppress("NOTHING_TO_INLINE", "DEPRECATION")
package net.aquadc.collections

import kotlin.jvm.JvmField
import kotlin.jvm.JvmSynthetic


inline class InlineEnumMap<K : Enum<K>, V>
@Deprecated("private") constructor(@PublishedApi internal val values: Array<Any? /* V | Unset */>)

// factory

inline fun <reified K : Enum<K>, V> enumMapOf(): InlineEnumMap<K, V> =
    InlineEnumMap(arrayOfUnset(enumValues<K>().size))

inline fun <reified K : Enum<K>, V> enumMapOf(
    k: K, v: V
): InlineEnumMap<K, V> {
    val values = arrayOfUnset(enumValues<K>().size)
    values[k.ordinal] = v
    return InlineEnumMap(values)
}

inline fun <reified K : Enum<K>, V> enumMapOf(
    k0: K, v0: V,
    k1: K, v1: V
): InlineEnumMap<K, V> {
    val values = arrayOfUnset(enumValues<K>().size)
    values[k0.ordinal] = v0
    values[k1.ordinal] = v1
    return InlineEnumMap(values)
}

inline fun <reified K : Enum<K>, V> enumMapOf(
    k0: K, v0: V,
    k1: K, v1: V,
    k2: K, v2: V
): InlineEnumMap<K, V> {
    val values = arrayOfUnset(enumValues<K>().size)
    values[k0.ordinal] = v0
    values[k1.ordinal] = v1
    values[k2.ordinal] = v2
    return InlineEnumMap(values)
}

inline fun <reified K : Enum<K>, V> enumMapOf(
    k0: K, v0: V,
    k1: K, v1: V,
    k2: K, v2: V,
    k3: K, v3: V
): InlineEnumMap<K, V> {
    val values = arrayOfUnset(enumValues<K>().size)
    values[k0.ordinal] = v0
    values[k1.ordinal] = v1
    values[k2.ordinal] = v2
    values[k3.ordinal] = v3
    return InlineEnumMap(values)
}

inline fun <reified K : Enum<K>, V> enumMapOf(
    k0: K, v0: V,
    k1: K, v1: V,
    k2: K, v2: V,
    k3: K, v3: V,
    k4: K, v4: V
): InlineEnumMap<K, V> {
    val values = arrayOfUnset(enumValues<K>().size)
    values[k0.ordinal] = v0
    values[k1.ordinal] = v1
    values[k2.ordinal] = v2
    values[k3.ordinal] = v3
    values[k4.ordinal] = v4
    return InlineEnumMap(values)
}

inline fun <reified K : Enum<K>, V> enumMapOf(
    k0: K, v0: V,
    k1: K, v1: V,
    k2: K, v2: V,
    k3: K, v3: V,
    k4: K, v4: V,
    k5: K, v5: V
): InlineEnumMap<K, V> {
    val values = arrayOfUnset(enumValues<K>().size)
    values[k0.ordinal] = v0
    values[k1.ordinal] = v1
    values[k2.ordinal] = v2
    values[k3.ordinal] = v3
    values[k4.ordinal] = v4
    values[k5.ordinal] = v5
    return InlineEnumMap(values)
}

inline fun <reified K : Enum<K>, V> enumMapOf(
    k0: K, v0: V,
    k1: K, v1: V,
    k2: K, v2: V,
    k3: K, v3: V,
    k4: K, v4: V,
    k5: K, v5: V,
    k6: K, v6: V
): InlineEnumMap<K, V> {
    val values = arrayOfUnset(enumValues<K>().size)
    values[k0.ordinal] = v0
    values[k1.ordinal] = v1
    values[k2.ordinal] = v2
    values[k3.ordinal] = v3
    values[k4.ordinal] = v4
    values[k5.ordinal] = v5
    values[k6.ordinal] = v6
    return InlineEnumMap(values)
}

inline fun <reified K : Enum<K>, V> enumMapOf(
    k0: K, v0: V,
    k1: K, v1: V,
    k2: K, v2: V,
    k3: K, v3: V,
    k4: K, v4: V,
    k5: K, v5: V,
    k6: K, v6: V,
    k7: K, v7: V
): InlineEnumMap<K, V> {
    val values = arrayOfUnset(enumValues<K>().size)
    values[k0.ordinal] = v0
    values[k1.ordinal] = v1
    values[k2.ordinal] = v2
    values[k3.ordinal] = v3
    values[k4.ordinal] = v4
    values[k5.ordinal] = v5
    values[k6.ordinal] = v6
    values[k7.ordinal] = v7
    return InlineEnumMap(values)
}

// write

fun <K : Enum<K>, V> InlineEnumMap<K, V>.put(key: K, value: V): V? {
    val index = key.ordinal
    val oldValue = values[index]
    values[index] = value
    return if (oldValue === Unset) null else oldValue as V
}

inline operator fun <K : Enum<K>, V> InlineEnumMap<K, V>.set(key: K, value: V) {
    values[key.ordinal] = value
}

fun <K : Enum<K>, V> InlineEnumMap<K, V>.remove(key: K): V? {
    val ord = key.ordinal
    val oldValue = values[ord]
    values[ord] = Unset
    return if (oldValue === Unset) null else oldValue as V
}

fun InlineEnumMap<*, *>.clear() {
    values.fill(Unset, 0, values.size)
}

// query

fun InlineEnumMap<*, *>.count(): Int = // O(n), like Iterable.count
    values.count { it !== Unset }

inline fun InlineEnumMap<*, *>.isEmpty(): Boolean =
    values.all { it === Unset }

inline fun <K : Enum<K>> InlineEnumMap<K, *>.containsKey(key: K): Boolean =
    values[key.ordinal] !== Unset

fun <V> InlineEnumMap<*, V>.containsValue(value: @UnsafeVariance V): Boolean =
    values.any { it !== Unset && it == value }

operator fun <K : Enum<K>, V> InlineEnumMap<K, V>.get(key: K): V? {
    val value = values[key.ordinal]
    return if (value === Unset) null else value as V
}

// not really views

fun <K : Enum<K>> InlineEnumMap<K, *>.keys(): InlineEnumSet<K> {
    require(values.size <= 64) { "enum type ${this::class} must have no more than 64 constants" }
    var set = 0L
    values.forEachIndexed { i, it ->
        if (it !== Unset)
            set = set or (1L shl i)
    }
    return InlineEnumSet(set)
}

inline fun <V> InlineEnumMap<*, V>.values(): Arr<V> =
    valuesTo(arrOfNulls<V>(count()))

@Suppress("UNCHECKED_CAST") @PublishedApi
internal fun <V> InlineEnumMap<*, V>.valuesTo(arr: Arr<in V>): Arr<V> {
    var src = 0
    var dest = 0
    val size = arr.size
    while (dest < size) {
        val value = values[src++]
        if (value !== Unset) {
            arr[dest++] = value as V
        }
    }
    return arr as Arr<V>
}

@Suppress("UNCHECKED_CAST")
inline fun <reified K : Enum<K>, V, R> InlineEnumMap<K, V>.map(transform: (K, V) -> R): Arr<R> {
    val arr = arrOfNulls<R>(count())
    var src = 0
    var dest = 0
    val size = arr.size
    if (size > 0) {
        val enums = enumValues<K>()
        while (dest < size) {
            val value = values[src]
            if (value !== Unset) {
                arr[dest++] = transform(enums[src], value as V)
            }
            src++
        }
    }
    return arr as Arr<R>
}

// util

@PublishedApi internal fun arrayOfUnset(size: Int): Array<Any?> =
    Array(size) { Unset } // let the whole for-loop be in this method

@PublishedApi @JvmField @JvmSynthetic internal val Unset = Any()
