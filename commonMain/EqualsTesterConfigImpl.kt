package dev.adokky.eqtester

import kotlin.jvm.JvmField

internal data class EqualsTesterConfigImpl(
    val groups: List<TestGroup>,
    override val requireNonIdentical: Boolean = false,
    override val checkToString: Boolean = false,
    override val checkCollectionContracts: Boolean = true,
    override val checkComparable: Boolean = true,
    override val maxIterations: Int = 100_000,
    override val defaultGroupSize: Int = 2
): EqualsTesterConfig {
    init {
        require(groups.isNotEmpty()) { "groups are empty" }
        require(groups.all { it.isNotEmpty() }) { "some groups are empty" }
        require(defaultGroupSize >= 2) { "'defaultGroupSize' should be >= 2" }
        require(maxIterations >= 1) { "'maxIterations' should be >= 1" }
    }

    companion object {
        @JvmField
        val Default = EqualsTesterConfigImpl(listOf(TestGroup.Simple(listOf(Unit))))
    }
}