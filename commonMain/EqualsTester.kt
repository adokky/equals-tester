package dev.adokky.eqtester

/**
 * Utility for testing `equals()`, `hashCode()`, and `toString()` contracts.
 *
 * [EqualsTester] is not intended to be used directly.
 * Use [testEquality] for a more flexible and readable way to define equality contracts:
 * ```
 * testEquality {
 *     group { User("Alice", 30) }
 *     group { User("Bob", 25) }
 *     checkToString = true
 * }
 * ```
 * @see EqualsTesterConfigBuilder
 * @see testEquality
 */
class EqualsTester {
    private val singleGroupChecker = SingleGroupChecker()
    private val multiGroupChecker = MultiGroupChecker()
    private val collectionChecker = CollectionContractChecker()

    /**
     * Tests a group of elements expected to be equal.
     *
     * All elements in the group must be equal to each other.
     *
     * @throws AssertionError if any contract is violated.
     */
    fun testElements(element0: Any, element1: Any, vararg rest: Any): Unit =
        testGroup(listOf(element0, element1) + rest)

    /**
     * Tests a group of elements expected to be equal.
     *
     * All elements in the group must be equal to each other.
     *
     * @throws AssertionError if any contract is violated.
     */
    fun testGroup(group: List<Any>): Unit = test(EqualsTesterConfigImpl(listOf(TestGroup.Simple(group))))

    /**
     * Tests multiple groups of elements, ensuring:
     *
     * - All elements within each group are equal.
     * - Elements from different groups are not equal.
     * - Hash codes differ between groups.
     *
     * @throws AssertionError if any contract is violated.
     */
    fun testGroups(group0: List<Any>, vararg rest: List<Any>): Unit =
        test(
            EqualsTesterConfigImpl(
                listOf(TestGroup.Simple(group0)) + rest.map { TestGroup.Simple(it) }
            )
        )

    internal fun test(config: EqualsTesterConfigImpl) {
        for (groupIndex in config.groups.indices) {
            singleGroupChecker.test(config, groupIndex)
        }

        if (config.collectionContractCheck) {
            collectionChecker.test(config)
        }

        if (config.groups.size > 1) {
            multiGroupChecker.test(config)
        }
    }
}