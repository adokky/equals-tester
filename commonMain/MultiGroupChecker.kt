package dev.adokky.eqtester

internal class MultiGroupChecker: PairTesterBase() {
    fun test(config: EqualsTesterConfigImpl) {
        setup(config)

        if (config.groups.size <= 1) return

        if (config.aproximateIterations() > config.maxIterations) {
            checkGroupsNotEqualProbabilistic(config.maxIterations)
            checkGroupsComparisonProbabilistic(config.maxIterations)
        } else {
            checkGroupsNotEqualTotal()
            checkGroupsComparisonTotal()
        }

        checkOrderOfGroups()
    }

    private fun checkGroupsNotEqualProbabilistic(iterations: Int) = repeat(iterations) {
        val (g1, g2) = config.groups.indices.randomPair()
        selectGroups(g1, g2)
        selectElements(group1.indices.random(), group2.indices.random())
        checkPairForNonEqual()
    }

    private fun checkGroupsNotEqualTotal() {
        forEachGroupPair {
            for (i1 in group1.indices)
            for (i2 in group2.indices) {
                selectElements(i1, i2)
                checkPairForNonEqual()
            }
        }
    }

    private fun checkPairForNonEqual() {
        if (element1 == element2) failure("are equal:\n${element1}\n${element2}")
        if (config.checkToString) {
            val s1 = element1.toString()
            if (s1 == element2.toString()) failure("have identical toString() results:\n$s1")
        }
    }

    private fun selectComparablePair() {
        val i1 = group1.indexOfFirst { it is Comparable<*> }
        val i2 = group2.indexOfFirst { it is Comparable<*> }
        if (i1 < 0 || i2 < 0) clearElements() else selectElements(i1, i2)
    }

    private fun checkGroupsComparisonProbabilistic(iterations: Int) = repeat(iterations) {
        val (g1, g2) = config.groups.indices.randomPair()
        selectGroups(g1, g2)

        val expectedDiff = getExpectedDiff()
        if (expectedDiff == 0) return@repeat // not comparable

        selectElements(group1.indices.random(), group2.indices.random())
        checkPairComparison(expectedDiff)
    }

    private fun checkGroupsComparisonTotal() {
        forEachGroupPair {
            val expectedDiff = getExpectedDiff()

            for (i1 in group1.indices)
            for (i2 in group2.indices) {
                selectElements(i1, i2)
                checkPairComparison(expectedDiff)
            }
        }
    }

    private fun getExpectedDiff(): Int {
        selectComparablePair()
        if (elementIndex1 < 0) return 0
        var expectedDiff = 0
        comparablePair { e1, e2 -> expectedDiff = e1.compareTo(e2) }
        if (expectedDiff == 0) throwCompareToIsZero()
        return expectedDiff
    }

    private fun checkPairComparison(expectedDiff: Int) {
        comparablePair { e1, e2 ->
            val cmpRes = e1.compareTo(e2)

            if (cmpRes == 0) throwCompareToIsZero()

            if (cmpRes != expectedDiff) throw AssertionError(
                "group[$groupName1].elements[$elementIndex1].compareTo(group[$groupName2].elements[$elementIndex2]) !=" +
                " group[$groupName1].elements[0].compareTo(group[$groupName2].elements[0]), ($expectedDiff != $cmpRes)"
            )
        }
    }

    private fun throwCompareToIsZero(): Nothing = throw AssertionError(
        "group[$groupName1].elements[$elementIndex1].compareTo(group[$groupName2].elements[$elementIndex2]) is zero"
    )

    private data class ComparableGroup(
        val elements: TestGroup,
        val comparable: Comparable<Any>,
        val index: Int
    ): Comparable<ComparableGroup> {
        override fun compareTo(other: ComparableGroup): Int =
            comparable.compareTo(other.comparable)
    }

    private fun checkOrderOfGroups() {
        var totalElements = 0

        val sorted = config.groups
            .mapIndexedNotNull { index, group ->
                @Suppress("UNCHECKED_CAST")
                (group.firstOrNull { it is Comparable<*> } as? Comparable<Any>)?.let {
                    totalElements += group.size
                    ComparableGroup(group, it, index)
                }
            }
            .sorted()

        val fullScan = config.aproximateIterations() < config.maxIterations

        for (i in sorted.indices) {
            val lessOrEqGroup = sorted[i]
            for (j in i+1 until sorted.size) {
                val greaterOrEqGroup = sorted[j]
                selectGroups(lessOrEqGroup.index, greaterOrEqGroup.index)
                val pairSize = lessOrEqGroup.elements.size + greaterOrEqGroup.elements.size
                if (fullScan || pairSize < 10) {
                    checkGroupPairOrderTotal()
                } else {
                    // (totalElements / pairSize) = (maxIterations / iterations)
                    val iterations = (pairSize * config.maxIterations / totalElements).coerceAtLeast(1)
                    checkGroupPairOrderProbabilistic(iterations)
                }
            }
        }
    }

    private fun checkGroupPairOrderTotal() {
        forEachElement {
            checkElementOrder()
        }
    }

    private fun checkGroupPairOrderProbabilistic(iterations: Int) = repeat(iterations) {
        selectElements(group1.indices.random(), group2.indices.random())
        checkElementOrder()
    }

    private fun checkElementOrder() {
        comparablePair { c1, c2 ->
            val cmp = c1.compareTo(c2)
            if (cmp >= 0) failure("comparison result is $cmp, but expected a negative number")

            val cmpInv = c2.compareTo(c1)
            if (cmpInv != -cmp) failure("comparison result is $cmp, but backward comparison result is $cmpInv")
        }
    }
}

internal fun EqualsTesterConfigImpl.aproximateIterations(): Long {
    var result = groups.size.toLong() * groups.size

    for (g in groups) {
        if (result > 1L shl 52) return Long.MAX_VALUE
        result *= g.size
    }

    return result
}