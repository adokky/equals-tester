package dev.adokky

internal class SingleGroupChecker: PairTesterBase() {
    fun test(config: EqualsTesterConfigImpl, groupIndex: Int) {
        require(groupIndex in config.groups.indices)

        setup(config)
        selectGroups(groupIndex, groupIndex)

        if (config.requireNonIdentical) {
            validateEquivalentsHasNoIdentical()
        }

        for (i in group1.indices) {
            setup(i)

            if (element1.equals(null)) failure("equals to null: ${element1}")

            if (element1 == PrivateUnit) failure("equals to foreign(unknown) type: ${element1}")

            val hash1 = element1.hashCode()
            val hash2 = element1.hashCode()
            if (hash1 != hash2) failure("return different hashCode(): $hash1, $hash2")
        }

        checkHashCodeEquality()

        val group = config.groups[groupIndex]
        if (group.size * group.size > config.maxIterations) {
            randomPairs(group.indices, config.maxIterations) { i1, i2 ->
                checkPair(i1, i2)
            }
        } else {
            allPairs(group.indices) { i1, i2 ->
                checkPair(i1, i2)
            }
        }
    }

    private fun checkPair(i1: Int, i2: Int) {
        selectElements(i1, i2)

        checkObjectAreEquals()
        checkComparable()
        checkToString()
    }

    private fun checkToString() {
        if (!config.checkToString) return
        val s1 = element1.toString()
        val s2 = element2.toString()
        if (s1 != s2) failure("have different toString() result.\nFirst:$s1\nSecond:$s2")
    }

    private fun setup(elementIndex: Int) {
        selectElements(elementIndex, elementIndex)
    }

    private fun checkHashCodeEquality() {
        val groupHash = group1.first().hashCode()
        for (i in 1 until group1.size) {
            selectElements(0, i)
            val hash = element2.hashCode()
            if (hash != groupHash) failure("have different hash code: $groupHash != $hash")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun checkComparable() {
        val cmp1 = element1 as? Comparable<Any> ?: return
        val cmp2 = element2 as? Comparable<Any> ?: return

        var res: Int = cmp1.compareTo(cmp2)
        if (res == 0) res = cmp2.compareTo(cmp1)

        if (res != 0) failure("return non-zero result for compareTo(): $res")
    }

    private fun validateEquivalentsHasNoIdentical() {
        allPairsExceptSelf(group1.indices) { i1, i2 ->
            selectElements(i1, i2)
            if (element1 === element2) throw IllegalArgumentException(
                failureMessage("are identical: $element1")
            )
        }
    }

    private fun checkObjectAreEquals() {
        if (element1 != element2 || element2 != element1) {
            if (element2 != PrivateUnit) {
                failure("are not equal, expected: <${element1}> got: <${element2}>\"")
            } else {
                failure("not equal to itself: $element1")
            }
        }
    }
}

