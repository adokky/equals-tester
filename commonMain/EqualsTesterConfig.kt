package dev.adokky.eqtester

interface EqualsTesterConfig {
    /** Require each equality group has no **identical** objects (equal by reference). `false` by default */
    val requireNonIdentical: Boolean

    /**
     * Large number equality groups or large number of elements in equality groups may imply huge number of test iterations
     * (non-linear grow, so may be effectively infinite).
     *
     * [EqualsTester] approximates number of such combinations and if it exceeds [maxIterations]
     * then, instead of checking all combinations of elements from all groups, [EqualsTester] does random sampling.
     * This setting limits the number of such random passes.
     *
     * You may consider reducing this number if you experience too long test runs.
     */
    val maxIterations: Int

    /** Require every object in each equality group has equivalent `toString()` result. `false` by default */
    val checkToString: Boolean

    /** Additional checks of [Map], [List], [Set] `hashCode()` and `equals()` contracts. `true` by default */
    val collectionContractCheck: Boolean
}