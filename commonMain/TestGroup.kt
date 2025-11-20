package dev.adokky.eqtester

internal sealed class TestGroup: Collection<Any> {
    abstract val name: String?
    abstract fun init(config: EqualsTesterConfig)
    protected abstract fun elements(): List<Any>

    operator fun get(index: Int): Any = elements()[index]

    val indices: IntRange get() = elements().indices

    final override fun iterator(): Iterator<Any> = elements().iterator()
    final override fun contains(element: Any): Boolean = elements().contains(element)
    final override fun containsAll(elements: Collection<Any>): Boolean = elements().containsAll(elements)
    final override fun isEmpty(): Boolean = elements().isEmpty()
    final override val size: Int get() = elements().size

    override fun equals(other: Any?): Boolean {
        if (other !is TestGroup) return false
        if (name != other.name) return false
        return elements() == other.elements()
    }

    override fun hashCode() = name.hashCode() * 31 + elements().hashCode()

    override fun toString() = name + elements()

    class Simple(
        private val elements: List<Any>,
        override val name: String? = null
    ): TestGroup() {
        override fun init(config: EqualsTesterConfig) {}

        override fun elements(): List<Any> = elements
    }

    class Lazy(
        private val elementFactory: (index: Int) -> Any,
        override val name: String? = null,
        private val customSize: Int? = null
    ): TestGroup() {
        private lateinit var elements: List<Any>

        override fun init(config: EqualsTesterConfig) {
            val size = customSize ?: config.defaultGroupSize
            elements = List(size, elementFactory)
        }

        override fun elements(): List<Any> = elements
    }

    companion object {
        operator fun invoke(values: List<Any>): Simple = Simple(values)
        operator fun invoke(vararg values: Any): Simple = Simple(values.toList())
    }
}