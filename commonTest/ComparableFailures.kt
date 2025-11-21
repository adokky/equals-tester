package dev.adokky.eqtester

import kotlin.test.Test

class ComparableFailures: BaseTest() {
    private class Comp(v: Int, val constCmp: Int? = null): BaseComparableBox<Comp>(v) {
        override fun compareTo(other: Comp): Int {
            if (constCmp != null) return constCmp
            return super.compareTo(other)
        }
    }

    private class MgBuggy(v: Int, val bug: Boolean = false): BaseComparableBox<MgBuggy>(v) {
        override fun compareTo(other: MgBuggy): Int {
            if (bug && other.v == 3) return 0
            return super.compareTo(other)
        }
    }

    @Test
    fun single_group_kill_switch() {
        testEquality {
            group(Comp(1), Comp(1), Comp(1, constCmp = 1), Comp(1))
            checkComparable = false
        }
    }

    @Test
    fun multi_group_kill_switch() {
        testEquality {
            group { MgBuggy(1) }
            group ( MgBuggy(2), MgBuggy(2, bug = true))
            group { MgBuggy(3) }
            checkComparable = false
        }
    }

    @Test
    fun compareTo_is_not_zero() {
        assertFailsWithMessage<AssertionError>(
            "elements at index=0 and index=2 return non-zero result for compareTo(): 1")
        {
            tester.testElements(Comp(1), Comp(1), Comp(1, constCmp = 1), Comp(1))
        }
    }

    @Test
    fun compare_to_itself_is_not_zero() {
        data class Buggy(val v: Int): Comparable<Buggy> {
            override fun compareTo(other: Buggy): Int {
                if (this === other) return -1
                return v - other.v
            }
        }

        assertFailsWithMessage<AssertionError>(
            "element at index=0 return non-zero result for compareTo(): -1")
        {
            tester.testElements(Buggy(1), Buggy(1), Buggy(1))
        }
    }

    @Test
    fun compareTo_is_zero() {
        assertFailsWithMessage<AssertionError>("is zero") {
            tester.testGroups(
                listOf(Comp(1), Comp(1), Comp(1)),
                listOf(Comp(2), Comp(2, constCmp = 0), Comp(2)),
                listOf(Comp(3), Comp(3), Comp(3))
            )
        }
    }

    @Test
    fun compare_two_groups_is_zero() {
        assertFailsWithMessage<AssertionError>(
            "group[1].elements[1].compareTo(group[2].elements[0]) is zero")
        {
            tester.testGroups(
                listOf(MgBuggy(1), MgBuggy(1), MgBuggy(1)),
                listOf(MgBuggy(2), MgBuggy(2, bug = true), MgBuggy(2)),
                listOf(MgBuggy(3), MgBuggy(3), MgBuggy(3))
            )
        }
    }

    @Test
    fun probabilistic() {
        assertFailsWithMessage<AssertionError>(" is zero") {
            tester.testGroups(
                (1..100).map { MgBuggy(1) },
                (1..100).map { MgBuggy(2) },
                (1..100).map { MgBuggy(3) },
                (1..30).map { MgBuggy(5) } + MgBuggy(5, bug = true) + (1..10).map { MgBuggy(5) },
                (1..100).map { MgBuggy(6) },
            )
        }
    }

    @Test
    fun unstable_compareTo() {
        class Buggy(v: Int, val off: Int = 0): BaseBox(v), Comparable<Buggy> {
            override fun compareTo(other: Buggy): Int = v - other.v - off
        }

        assertFailsWithMessage<AssertionError>("elements at index=0 and index=1 of group 1") {
            tester.testGroups(
                listOf(Buggy(1), Buggy(1), Buggy(1)),
                listOf(Buggy(2), Buggy(2, 1), Buggy(2)),
                listOf(Buggy(3), Buggy(3), Buggy(3))
            )
        }
    }

    @Test
    fun total_order_failure() {
        data class Buggy(val v: Int): Comparable<Buggy> {
            override fun compareTo(other: Buggy): Int {
                if (v == 1 && other.v == 3) return 2
                if (v == 3 && other.v == 1) return -2
                return v - other.v
            }
        }

        // (1 > 2 > 3) & (1 > 3)
        assertFailsWithMessage<AssertionError>(
            "comparison result is 2, but expected a negative number")
        {
            tester.testGroups(
                listOf(Buggy(1), Buggy(1), Buggy(1)),
                listOf(Buggy(2), Buggy(2), Buggy(2)),
                listOf(Buggy(3), Buggy(3), Buggy(3))
            )
        }
    }
}