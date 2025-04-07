class OpenAddressingHashMap<K, V> : MutableMap<K, V> {

    override val keys: MutableSet<K>
        get() = TODO("Not yet implemented")

    override val values: MutableCollection<V>
        get() = TODO("Not yet implemented")

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = TODO("Not yet implemented")

    override fun put(key: K, value: V): V? {
        val index = findSlot(key)
        slots[index]?.let { slot ->
            if (slot.isOccupied) {
                return slot.setValue(value)
            } else {
                slot.key = key
                slot.value = value
                slot.isOccupied = true
                return null
            }
        }
        slots[index] = OpenAddressingEntry(key, value, true)
        return null
    }

    override fun remove(key: K): V? {
        TODO("Not yet implemented")
    }

    override fun putAll(from: Map<out K, V>) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override val size: Int
        get() = TODO("Not yet implemented")

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsKey(key: K): Boolean {
        val index = findSlot(key)
        slots[index]?.also { slot ->
            return slot.isOccupied
        }
        return false
    }

    override fun containsValue(value: V): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(key: K): V? {
        val index = findSlot(key)
        slots[index]?.also { slot ->
            if (slot.isOccupied) {
                return slot.value
            }
        }
        return null
    }

    private val capacity = 10

    private val slots = Array<OpenAddressingEntry<K, V>?>(capacity) { null }

    private fun findSlot(key: K): Int {
        var index = key.hashCode() % capacity
        while (slots[index]?.isOccupied == true && slots[index]?.key != key) {
            index = (index + 1) % capacity
        }
        return index
    }
}