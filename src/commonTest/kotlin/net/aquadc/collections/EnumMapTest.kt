package net.aquadc.collections

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EnumMapTest {

    @Test fun empty() {
        val map = enumMapOf<LazyThreadSafetyMode, String>()

        check(map, emptyArr(), emptyArr())

        assertEquals(null, map.put(LazyThreadSafetyMode.NONE, "none"))
        assertEquals("none", map[LazyThreadSafetyMode.NONE])
        assertEquals("none", map.put(LazyThreadSafetyMode.NONE, "None"))
        check(map, arrOf(LazyThreadSafetyMode.NONE), arrOf("None"))

        map[LazyThreadSafetyMode.PUBLICATION] = "pub"
        assertEquals("pub", map[LazyThreadSafetyMode.PUBLICATION])
        check(map, arrOf(LazyThreadSafetyMode.PUBLICATION, LazyThreadSafetyMode.NONE), arrOf("pub", "None"))

        assertEquals(null, map.remove(LazyThreadSafetyMode.SYNCHRONIZED))
        assertEquals("pub", map.remove(LazyThreadSafetyMode.PUBLICATION))
        assertEquals(null, map.remove(LazyThreadSafetyMode.PUBLICATION))

        map.clear()
        check(map, emptyArr(), emptyArr())
    }

    private val randomValues = arrOf("", "none", "None", "nope", "pub", "fn")
    private fun check(map: InlineEnumMap<LazyThreadSafetyMode, String>, keys: Arr<LazyThreadSafetyMode>, values: Arr<String>) {
        check(keys.size == values.size)
        assertEquals(keys, map.keys().toArr())
        assertEquals(values, map.values())
        assertEquals(keys.size, map.count())
        assertEquals(keys.isEmpty(), map.isEmpty())
        assertEquals(keys.zip(values), map.map(::Pair))

        assertTrue(keys.all(map::containsKey))
        assertFalse((arrOf(*enumValues<LazyThreadSafetyMode>()) - keys).any(map::containsKey))

        assertTrue(values.all(map::containsValue))
        assertFalse((randomValues - values).any(map::containsValue))
    }

}
