package dev.adokky.eqtester

class EqualsTesterConfigBuilder internal constructor(private val defaultGroupSize: Int = DEFAULT_GROUP_SIZE): EqualsTesterConfig {
    internal val groups = ArrayList<List<Any>>()

    override var requireNonIdentical: Boolean = EqualsTesterConfigImpl.Default.requireNonIdentical

    override var maxIterations: Int = EqualsTesterConfigImpl.Default.maxIterations

    override var checkToString: Boolean = EqualsTesterConfigImpl.Default.checkToString

    override var collectionContractCheck: Boolean = EqualsTesterConfigImpl.Default.collectionContractCheck

    fun group(size: Int = defaultGroupSize, elementFactory: () -> Any) {
        groups.add((1..size).map { elementFactory() })
    }

    fun group(data: List<Any>) { groups.add(data) }

    fun group(element0: Any, element1: Any, vararg rest: Any) {
        group(listOf(element0, element1) + rest)
    }

    fun groups(groups: Iterable<Iterable<Any>>) {
        for (g in groups) {
            this.groups.add(g as? List ?: g.toList())
        }
    }

    internal fun toConfig() = EqualsTesterConfigImpl(
        groups = groups,
        requireNonIdentical = requireNonIdentical,
        maxIterations = maxIterations,
        checkToString = checkToString,
        collectionContractCheck = collectionContractCheck
    )

    companion object {
        internal const val DEFAULT_GROUP_SIZE = 3
    }
}