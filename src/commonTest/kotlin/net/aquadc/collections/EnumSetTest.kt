package net.aquadc.collections

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EnumSetTest {

    enum class E {
        A, B, C, D, E, F, G
    }

    @Test fun empty() {
        val empty = noneOf<E>()
        assertEquals(0, empty.size)
        assertTrue(empty.isEmpty)
        assertEquals<List<E>>(emptyList(), empty.toArr())
        assertFalse(E.A in empty)
        assertFalse(E.B in empty)
        assertFalse(E.C in empty)
        assertFalse(E.D in empty)
        assertFalse(E.E in empty)
        assertFalse(E.F in empty)
        assertFalse(E.G in empty)
        assertFalse(empty.containsAll(E.A + E.B))
    }

    @Test fun single() {
        val single = E.C.asSet()
        assertEquals(1, single.size)
        assertFalse(single.isEmpty)
        assertEquals(listOf(E.C), single.toArr())
        assertFalse(E.A in single)
        assertFalse(E.B in single)
        assertTrue(E.C in single)
        assertFalse(E.D in single)
        assertFalse(E.E in single)
        assertFalse(E.F in single)
        assertFalse(E.G in single)
        assertTrue(single.containsAll(E.C.asSet()))
        assertFalse(single.containsAll(E.A + E.B))
    }
    
    @Test fun several() {
        val several = (E.A + E.B + E.C) + (E.F + E.G) intersect (E.B + (E.F + E.G)) union E.D + E.C subtract E.C.asSet()
        assertEquals(4, several.size)
        assertFalse(several.isEmpty)
        assertEquals(listOf(E.B, E.D, E.F, E.G), several.toArr())
        assertFalse(E.A in several)
        assertTrue(E.B in several)
        assertFalse(E.C in several)
        assertTrue(E.D in several)
        assertFalse(E.E in several)
        assertTrue(E.F in several)
        assertTrue(E.G in several)
        assertTrue(several.containsAll(E.B + E.D))
        assertFalse(several.containsAll(E.A + E.D))
    }
    
    @Test fun full() {
        val full = allOf<E>()
        assertEquals(7, full.size)
        assertFalse(full.isEmpty)
        assertTrue(E.A + E.B + E.C + E.D + E.E + E.F + E.G eq full)
        assertEquals(listOf(E.A, E.B, E.C, E.D, E.E, E.F, E.G), full.toArr())
        assertTrue(E.A in full)
        assertTrue(E.B in full)
        assertTrue(E.C in full)
        assertTrue(E.D in full)
        assertTrue(E.E in full)
        assertTrue(E.F in full)
        assertTrue(E.G in full)
        assertTrue(full.containsAll(E.B + E.D))
        assertTrue(full.containsAll(E.A + E.D))
    }

}
