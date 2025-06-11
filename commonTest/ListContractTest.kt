package dev.adokky

import kotlin.test.Test

class ListContractTest {
    private class BuggyList(
        val content: List<Int>,
        val buggyHash: Boolean = false,
        val buggyEquals: Boolean = false
    ): List<Int> by content {
        override fun equals(other: Any?): Boolean = when {
            buggyEquals -> {
                (other as? BuggyList)?.content?.equals(content) == true
            }
            else -> content.equals(other)
        }

        override fun hashCode(): Int = when {
            buggyHash -> {
                content.fold(1) { acc, i -> acc * i + 3 }
            }
            else -> content.hashCode()
        }

        override fun toString() = content.toString()
    }

    @Test
    fun ok() {
        testEquality(defaultGroupSize = 2) {
            group { BuggyList(listOf(45, 76, 22409)) }
            group { BuggyList(listOf(1)) }
            group { BuggyList(listOf(1, 2, 3)) }
        }
    }

    @Test
    fun invalid_hash_code() {
        assertFailsWithMessage<AssertionError>("List.hashCode") {
            testEquality(defaultGroupSize = 2) {
                group { BuggyList(listOf(45, 76, 22409), buggyHash = true) }
                group { BuggyList(listOf(1), buggyHash = true) }
                group { BuggyList(listOf(1, 2, 3), buggyHash = true) }
            }
        }
    }

    @Test
    fun invalid_equals() {
        assertFailsWithMessage<AssertionError>("List.equals") {
            testEquality(defaultGroupSize = 2) {
                group { BuggyList(listOf(45, 76, 22409), buggyEquals = true) }
                group { BuggyList(listOf(1), buggyEquals = true) }
                group { BuggyList(listOf(1, 2, 3), buggyEquals = true) }
            }
        }
    }
}