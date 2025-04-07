import kotlin.test.*
import kotlin.collections.MutableMap.MutableEntry

class OpenAddressingEntryTest {
    @Test
    fun testConstructor() {
        val entry = OpenAddressingEntry("key", "value")
        assertEquals("key", entry.key)
        assertEquals("value", entry.value)
        assertFalse(entry.isOccupied)

        val occupiedEntry = OpenAddressingEntry("key", "value", true)
        assertTrue(occupiedEntry.isOccupied)
    }

    @Test
    fun testSetValue() {
        val entry = OpenAddressingEntry("key", "value")
        val oldValue = entry.setValue("newValue")
        assertEquals("value", oldValue)
        assertEquals("newValue", entry.value)
    }

    @Test
    fun testKeyAndValueReassignment() {
        val entry = OpenAddressingEntry("key", "value")
        entry.key = "newKey"
        entry.value = "newValue"
        assertEquals("newKey", entry.key)
        assertEquals("newValue", entry.value)
    }
}

class OpenAddressingHashMapTest {
    @Test
    fun testPut() {
        val map = OpenAddressingHashMap<String, Int>()
        assertNull(map.put("one", 1))
        assertEquals(1, map.get("one"))

        // Test overwriting an existing key
        assertEquals(1, map.put("one", 11))
        assertEquals(11, map.get("one"))
    }

    @Test
    fun testGet() {
        val map = OpenAddressingHashMap<String, Int>()
        assertNull(map.get("one"))

        map.put("one", 1)
        assertEquals(1, map.get("one"))

        // Test getting a key that doesn't exist after hash collision
        map.put("two", 2)  // Might cause a collision depending on hashCode
        assertNull(map.get("three"))
    }

    @Test
    fun testContainsKey() {
        val map = OpenAddressingHashMap<String, Int>()
        assertFalse(map.containsKey("one"))

        map.put("one", 1)
        assertTrue(map.containsKey("one"))
        assertFalse(map.containsKey("two"))
    }

    @Test
    fun testCollisionHandling() {
        // Create a custom class with a fixed hashCode to force collisions
        class FixedHashKey(val value: String) {
            override fun hashCode(): Int = 5  // Always return the same hash
            override fun equals(other: Any?): Boolean =
                other is FixedHashKey && value == other.value

            override fun toString(): String = value
        }

        val map = OpenAddressingHashMap<FixedHashKey, Int>()
        val key1 = FixedHashKey("one")
        val key2 = FixedHashKey("two")
        val key3 = FixedHashKey("three")

        map.put(key1, 1)
        map.put(key2, 2)
        map.put(key3, 3)

        assertEquals(1, map.get(key1))
        assertEquals(2, map.get(key2))
        assertEquals(3, map.get(key3))
    }

    @Test
    fun testNullValues() {
        val map = OpenAddressingHashMap<String, Int?>()
        assertNull(map.put("null", null))
        assertNull(map.get("null"))
        assertTrue(map.containsKey("null"))

        assertNull(map.put("null", 1))
        assertEquals(1, map.get("null"))
    }

    @Test
    fun testMapCapacity() {
        // The map has a fixed capacity of 10, let's test behavior when adding elements
        val map = OpenAddressingHashMap<Int, Int>()

        // Add 9 entries (leaving one slot open)
        for (i in 0 until 9) {
            map.put(i, i)
        }

        // Verify all entries are accessible
        for (i in 0 until 9) {
            assertEquals(i, map.get(i))
        }

        // Add the 10th entry
        map.put(9, 9)
        assertEquals(9, map.get(9))
    }

    @Test
    fun testFindSlotAlgorithm() {
        // Create strings that will hash to the same bucket
        class CollisionString(private val value: String) {
            override fun hashCode(): Int = 1 // All values hash to 1
            override fun equals(other: Any?): Boolean =
                other is CollisionString && value == other.value
            override fun toString(): String = value
        }

        // Test that linear probing works correctly when slots are occupied
        val map = OpenAddressingHashMap<CollisionString, Int>()

        val key1 = CollisionString("first")
        val key2 = CollisionString("second")
        val key3 = CollisionString("third")

        // Insert keys that will collide
        map.put(key1, 1)
        map.put(key2, 2)
        map.put(key3, 3)

        // Verify we can retrieve them all
        assertEquals(1, map.get(key1))
        assertEquals(2, map.get(key2))
        assertEquals(3, map.get(key3))

        // Verify a new key with same hash but not equal will not be found
        assertNull(map.get(CollisionString("fourth")))
    }

    // Test stubs for the unimplemented methods

    @Test
    @Ignore("Not yet implemented")
    fun testRemove() {
        val map = OpenAddressingHashMap<String, Int>()
        map.put("one", 1)
        assertEquals(1, map.remove("one"))
        assertNull(map.get("one"))
        assertNull(map.remove("nonexistent"))
    }

    @Test
    @Ignore("Not yet implemented")
    fun testPutAll() {
        val map = OpenAddressingHashMap<String, Int>()
        val sourceMap = mapOf("one" to 1, "two" to 2)
        map.putAll(sourceMap)
        assertEquals(1, map.get("one"))
        assertEquals(2, map.get("two"))
    }

    @Test
    @Ignore("Not yet implemented")
    fun testClear() {
        val map = OpenAddressingHashMap<String, Int>()
        map.put("one", 1)
        map.put("two", 2)
        map.clear()
        assertNull(map.get("one"))
        assertNull(map.get("two"))
        assertTrue(map.isEmpty())
    }

    @Test
    @Ignore("Not yet implemented")
    fun testSize() {
        val map = OpenAddressingHashMap<String, Int>()
        assertEquals(0, map.size)
        map.put("one", 1)
        assertEquals(1, map.size)
        map.put("two", 2)
        assertEquals(2, map.size)
        map.put("one", 11) // Update existing
        assertEquals(2, map.size)
    }

    @Test
    @Ignore("Not yet implemented")
    fun testIsEmpty() {
        val map = OpenAddressingHashMap<String, Int>()
        assertTrue(map.isEmpty())
        map.put("one", 1)
        assertFalse(map.isEmpty())
    }

    @Test
    @Ignore("Not yet implemented")
    fun testContainsValue() {
        val map = OpenAddressingHashMap<String, Int>()
        assertFalse(map.containsValue(1))
        map.put("one", 1)
        assertTrue(map.containsValue(1))
        assertFalse(map.containsValue(2))
    }

    @Test
    @Ignore("Not yet implemented")
    fun testKeys() {
        val map = OpenAddressingHashMap<String, Int>()
        assertTrue(map.keys.isEmpty())
        map.put("one", 1)
        map.put("two", 2)
        assertEquals(setOf("one", "two"), map.keys)
    }

    @Test
    @Ignore("Not yet implemented")
    fun testValues() {
        val map = OpenAddressingHashMap<String, Int>()
        assertTrue(map.values.isEmpty())
        map.put("one", 1)
        map.put("two", 2)
        assertTrue(map.values.containsAll(listOf(1, 2)))
        assertEquals(2, map.values.size)
    }

    @Test
    @Ignore("Not yet implemented")
    fun testEntries() {
        val map = OpenAddressingHashMap<String, Int>()
        assertTrue(map.entries.isEmpty())
        map.put("one", 1)
        map.put("two", 2)
        assertEquals(2, map.entries.size)

        val entrySet = map.entries
        assertTrue(entrySet.any { it.key == "one" && it.value == 1 })
        assertTrue(entrySet.any { it.key == "two" && it.value == 2 })
    }
}