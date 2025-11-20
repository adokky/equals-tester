package dev.adokky.eqtester

internal abstract class PairTesterBase {
    protected lateinit var config: EqualsTesterConfigImpl
        private set

    protected var groupIndex1 = -1
        private set
    protected var groupIndex2 = -1
        private set

    protected var elementIndex1 = -1
        private set
    protected var elementIndex2 = -1
        private set

    protected fun setup(config: EqualsTesterConfigImpl) {
        this.config = config
        groupIndex1 = -1
        groupIndex2 = -1
        clearElements()
    }

    protected fun selectGroups(index1: Int, index2: Int) {
        this.groupIndex1 = index1
        this.groupIndex2 = index2

        clearElements()
    }

    protected fun selectElements(index1: Int, index2: Int) {
        checkGroupIsSelected()

        this.elementIndex1 = index1
        this.elementIndex2 = index2
    }

    protected fun clearElements() {
        this.elementIndex1 = -1
        this.elementIndex2 = -1
    }

    private fun checkGroupIsSelected() {
        require(groupIndex1 != -1) { "must select group first" }
    }

    protected val group1: TestGroup get() = config.groups[groupIndex1]
    protected val group2: TestGroup get() = config.groups[groupIndex2]

    protected val element1: Any get() = group1[elementIndex1]
    protected val element2: Any get() = group2[elementIndex2]

    protected val comparableOrNull1: Comparable<Any>? get() = comparableOrNull1(elementIndex1)
    protected val comparableOrNull2: Comparable<Any>? get() = comparableOrNull2(elementIndex2)

    protected val groupName1: String get() = group1.name?.let { "'$it'" } ?: groupIndex1.toString()
    protected val groupName2: String get() = group2.name?.let { "'$it'" } ?: groupIndex2.toString()

    @Suppress("UNCHECKED_CAST")
    protected fun comparableOrNull1(index: Int): Comparable<Any>? = group1[index] as? Comparable<Any>
    @Suppress("UNCHECKED_CAST")
    protected fun comparableOrNull2(index: Int): Comparable<Any>? = group2[index] as? Comparable<Any>

    protected inline fun comparablePair(body: (e1: Comparable<Any>, e2: Comparable<Any>) -> Unit) {
        val cmp1 = comparableOrNull1 ?: return
        val cmp2 = comparableOrNull2 ?: return
        body(cmp1, cmp2)
    }

    object ForeignObject

    protected fun failure(reason: String) {
        throw AssertionError(failureMessage(reason))
    }

    // elements at index 0 of group 1, and index 2 of group 3 are equal
    protected fun failureMessage(reason: String): String = buildString {
        if (groupIndex1 == groupIndex2) {
            append(
                when {
                    elementIndex2 != elementIndex1 -> {
                        "elements at index=$elementIndex1 and index=$elementIndex2"
                    }
                    else -> "element at index=$elementIndex1"
                }
            )

            if (config.groups.size > 1) {
                append(" of group $groupName1")
            }
        } else {
            append("elements at index=$elementIndex1 of group=$groupName1 and index=$elementIndex2 of group=$groupName2")
        }

        append(' ')
        append(reason)
    }

    protected inline fun forEachGroupPair(body: () -> Unit) {
        allPairsExceptSelf(config.groups.indices) { gi1, gi2 ->
            selectGroups(gi1, gi2)
            body()
        }
    }

    protected inline fun forEachElement(body: () -> Unit) {
        checkGroupIsSelected()
        for (i1 in group1.indices)
        for (i2 in group2.indices) {
            selectElements(i1, i2)
            body()
        }
    }
}