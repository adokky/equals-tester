package dev.adokky.eqtester

import kotlin.jvm.JvmField

data class EqualsTesterConfigImpl(
    val groups: List<List<Any>>,
    override val requireNonIdentical: Boolean = false,
    override val checkToString: Boolean = false,
    override val collectionContractCheck: Boolean = true,
    override val maxIterations: Int = 1_000_000
): EqualsTesterConfig {
    init {
        require(groups.isNotEmpty()) { "groups are empty" }
        require(groups.all { it.isNotEmpty() }) { "some groups are empty" }
    }

    internal companion object {
        @JvmField
        val Default = EqualsTesterConfigImpl(listOf(listOf(Unit)))
    }
}