package dev.adokky.eqtester

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class GoodCases: BaseTest() {
    @Test
    fun single_group() {
        for (n in 1..5) {
            test(*((1..n).map { ComparableBox(n) }.toTypedArray()))
        }
    }

    @Test
    fun non_comparable_full() {
        testEquality {
            groupOfSize(5) { Box(1) }
            groupOfSize(2) { Box(2) }
            groupOfSize(2) { Box(3) }
            groupOfSize(3) { Box(4) }
            checkToString = true
        }
    }

    @Test
    fun non_comparable_probabilistic() {
        EqualsTester().testGroups(
            (1..40).map { Box(1) },
            (1..10).map { Box(2) },
            (1..30).map { Box(3) },
            (1..100).map { Box(4) },
            (1..130).map { Box(5) },
        )
    }

    @Test
    fun test_lists() {
        test(listOf(ComparableBox(1)), listOf(ComparableBox(1)), listOf(ComparableBox(1)))
    }

    @Test
    fun multiple_groups() {
        testEquality {
            groupOfSize(2) { ComparableBox(1) }
            groupOfSize(1) { ComparableBox(2) }
            groupOfSize(3) { ComparableBox(3) }
        }
    }

    @Test
    fun large_groups() {
        EqualsTester().test(
            EqualsTesterConfigImpl(
                groups = (1..100)
                    .map { groupIndex -> randomGroup(groupIndex) }
                    .plusElement((1..1000).map { ComparableBox(101) })
                    .map { TestGroup.Simple(it) },
                maxIterations = 100_000
            )
        )
    }

    @Test
    fun cardinality_test() {
        val groups: List<TestGroup> = (1..100)
            .map { groupIndex -> randomGroup(groupIndex) }
            .plusElement((1..1000).map { ComparableBox(101) })
            .map { TestGroup.Simple(it) }

        assertEquals(Long.MAX_VALUE, EqualsTesterConfigImpl(groups).aproximateIterations())

        assertEquals(3 * 3 * 2 * 1 * 3, EqualsTesterConfigImpl(
            listOf(
                TestGroup.Simple(listOf(ComparableBox(1), ComparableBox(1))),
                TestGroup.Simple(listOf(ComparableBox(2))),
                TestGroup.Simple(listOf(ComparableBox(3), ComparableBox(3), ComparableBox(3)))
            )
        ).aproximateIterations())
    }

    private fun randomGroup(groupIndex: Int): List<ComparableBox> = (1..Random.nextInt(100, 1000)).map { ComparableBox(groupIndex) }
}