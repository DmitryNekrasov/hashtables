import kotlin.collections.MutableMap.MutableEntry

class OpenAddressingEntry<K, V>(override var key: K, override var value: V, var isOccupied: Boolean = false) : MutableEntry<K, V> {
    override fun setValue(newValue: V): V {
        val oldValue = value
        value = newValue
        return oldValue
    }
}