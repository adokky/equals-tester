package dev.adokky

/**
 * Note: there is no way to implement a one-sided equals() correctly with inheritance -
 * it's a broken concept, but it's still used so often that you have to do your best with it.
 */
class EqualsTester {
    private val singleGroupChecker = SingleGroupChecker()
    private val multiGroupChecker = MultiGroupChecker()
    private val collectionChecker = CollectionContractChecker()

    fun testElements(element0: Any, element1: Any, vararg rest: Any) =
        testGroup(listOf(element0, element1) + rest)

    fun testGroup(group: List<Any>) = test(EqualsTesterConfigImpl(listOf(group)))

    fun testGroups(group0: List<Any>, vararg rest: List<Any>) =
        test(EqualsTesterConfigImpl(listOf(group0) + rest))

    fun test(config: EqualsTesterConfigImpl) {
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