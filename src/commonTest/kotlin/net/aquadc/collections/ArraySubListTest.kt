package net.aquadc.collections

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue


class ArraySubListTest {

    @Test fun negativeFrom() { assertFailsWith(IndexOutOfBoundsException::class) { arrayOf<Any>().subList(-1, -1) } }
    @Test fun bigTo() { assertFailsWith(IndexOutOfBoundsException::class) { arrayOf<Any>().subList(0, 1) } }
    @Test fun fromGtTo() { assertFailsWith(IllegalArgumentException::class) { arrayOf<Any>("").subList(1, 0) } }

    @Test fun empty() = assertSame(emptyList(), arrayOf<Any>().subList(0, 0))
    @Test fun emptySlice0() = assertSame(listOf(), arrayOf<Any>().subList(0, 0))
    @Test fun emptySlice1() = assertSame(listOf(), arrayOf<Any>("").subList(1, 1))

    @Test fun full() {
        val subList = arrayOf("").subList(0, 1)
        assertEquals(listOf(""), subList)
        assertEquals(1, subList.size)
        assertFalse(subList.isEmpty())

        assertFailsWith(IndexOutOfBoundsException::class) { subList[-1] }
        assertEquals("", subList[0])
        assertFailsWith(IndexOutOfBoundsException::class) { subList[1] }

        assertTrue("" in subList)
        assertFalse("whatever" in subList)

        assertTrue(subList.containsAll(listOf("")))
        assertTrue(subList.containsAll(listOf("", "")))
        assertFalse(subList.containsAll(listOf("whatever")))
        assertFalse(subList.containsAll(listOf("", "whatever")))

        assertTrue(arrayOf("").contentEquals(subList.toTypedArray()))

        val i = subList.iterator()
        assertTrue(i.hasNext())
        assertEquals("", i.next())
        assertFalse(i.hasNext())
        assertFailsWith(NoSuchElementException::class) { i.next() }

        val li = subList.listIterator()
        assertFalse(li.hasPrevious())
        assertFailsWith(NoSuchElementException::class) { li.previous() }
        assertTrue(li.hasNext())
        assertEquals(0, li.nextIndex())
        assertEquals("", li.next())

        assertTrue(li.hasPrevious())
        assertFalse(li.hasNext())
        assertFailsWith(NoSuchElementException::class) { li.next() }
        assertEquals(0, li.previousIndex())
        assertEquals("", li.previous())

        val lii = subList.listIterator(1)
        assertTrue(lii.hasPrevious())
        assertFalse(lii.hasNext())
        assertFailsWith(NoSuchElementException::class) { lii.next() }
        assertEquals(0, lii.previousIndex())
        assertEquals("", lii.previous())

        assertEquals(-1, subList.indexOf("zzz"))
        assertEquals(0, subList.indexOf(""))
        assertEquals(0, subList.lastIndexOf(""))

        assertEquals(emptyList(), subList.subList(0, 0))
        assertEquals(emptyList(), subList.subList(1, 1))
        assertEquals(listOf(""), subList.subList(0, 1))
    }

    @Test fun end() {
        val subList = arrayOf("1", "2", "3").subList(1, 3)
        assertEquals(listOf("2", "3"), subList)
        assertEquals(2, subList.size)
        assertFalse(subList.isEmpty())

        assertFailsWith(IndexOutOfBoundsException::class) { subList[-1] }
        assertEquals("2", subList[0])
        assertEquals("3", subList[1])
        assertFailsWith(IndexOutOfBoundsException::class) { subList[2] }

        assertFalse("1" in subList)
        assertTrue("2" in subList)
        assertTrue("3" in subList)
        assertFalse("whatever" in subList)

        assertTrue(subList.containsAll(listOf("2")))
        assertTrue(subList.containsAll(listOf("3")))
        assertTrue(subList.containsAll(listOf("2", "2")))
        assertTrue(subList.containsAll(listOf("2", "3")))
        assertTrue(subList.containsAll(listOf("3", "2")))
        assertTrue(subList.containsAll(listOf("3", "3")))
        assertTrue(subList.containsAll(listOf("2", "2", "3")))
        assertFalse(subList.containsAll(listOf("1")))
        assertFalse(subList.containsAll(listOf("1", "2")))

        assertTrue(arrayOf("2", "3").contentEquals(subList.toTypedArray()))

        val i = subList.iterator()
        assertTrue(i.hasNext())
        assertEquals("2", i.next())
        assertTrue(i.hasNext())
        assertEquals("3", i.next())
        assertFalse(i.hasNext())
        assertFailsWith(NoSuchElementException::class) { i.next() }

        val li = subList.listIterator()
        assertFalse(li.hasPrevious())
        assertFailsWith(NoSuchElementException::class) { li.previous() }
        assertTrue(li.hasNext())
        assertEquals(0, li.nextIndex())
        assertEquals("2", li.next())

        assertTrue(li.hasPrevious())
        assertTrue(li.hasNext())
        assertEquals(1, li.nextIndex())
        assertEquals("3", li.next())

        assertTrue(li.hasPrevious())
        assertFalse(li.hasNext())
        assertFailsWith(NoSuchElementException::class) { li.next() }
        assertEquals(1, li.previousIndex())
        assertEquals("3", li.previous())

        assertEquals(0, li.previousIndex())
        assertEquals("2", li.previous())
    }

}
