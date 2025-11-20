package dev.adokky.eqtester

import kotlin.test.Test

class SetContractTest {
    private class BuggySet(
        val content: Set<Int>,
        val buggyHash: Boolean = false,
        val buggyEquals: Boolean = false
    ): Set<Int> by content {
        override fun equals(other: Any?): Boolean = when {
            buggyEquals -> {
                (other as? BuggySet)?.content?.equals(content) == true
            }
            else -> content.equals(other)
        }

        override fun hashCode(): Int = when {
            buggyHash -> {
                content.fold(0) { acc, i -> acc + i * 2 }
            }
            else -> content.hashCode()
        }

        override fun toString() = content.toString()
    }

    @Test
    fun ok() {
        testEquality {
            group { BuggySet(setOf(45, 76, 22409)) }
            group { BuggySet(setOf(1)) }
            group { BuggySet(setOf(1, 2, 3)) }
        }
    }

    @Test
    fun invalid_hash_code() {
        assertFailsWithMessage<AssertionError>("Set.hashCode") {
            testEquality {
                group { BuggySet(setOf(45, 76, 22409), buggyHash = true) }
                group { BuggySet(setOf(1), buggyHash = true) }
                group { BuggySet(setOf(1, 2, 3), buggyHash = true) }
            }
        }
    }

    @Test
    fun invalid_equals() {
        assertFailsWithMessage<AssertionError>("Set.equals") {
            testEquality {
                group { BuggySet(setOf(45, 76, 22409), buggyEquals = true) }
                group { BuggySet(setOf(1), buggyEquals = true) }
                group { BuggySet(setOf(1, 2, 3), buggyEquals = true) }
            }
        }
    }
}