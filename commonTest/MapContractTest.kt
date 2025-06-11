package dev.adokky

import kotlin.test.Test

class MapContractTest {
    private class BuggyMap(
        val content: Map<Int, Int>,
        val buggyHash: Boolean = false,
        val buggyEquals: Boolean = false
    ): Map<Int, Int> by content {
        override fun equals(other: Any?): Boolean = when {
            buggyEquals -> {
                (other as? BuggyMap)?.content?.equals(content) == true
            }
            else -> content.equals(other)
        }

        override fun hashCode(): Int = when {
            buggyHash -> {
                content.entries.fold(0) { acc, entry -> acc + entry.hashCode() * 2 + 7 }
            }
            else -> content.hashCode()
        }

        override fun toString() = content.toString()
    }

    private fun EqualsTesterConfigBuilder.setup(
        buggyHash: Boolean = false,
        buggyEquals: Boolean = false
    ) {
        group { BuggyMap(mapOf(45 to 3, 76 to 4, 22409 to 0), buggyHash = buggyHash, buggyEquals = buggyEquals) }
        group { BuggyMap(mapOf(1 to 2), buggyHash = buggyHash, buggyEquals = buggyEquals) }
        group { BuggyMap(mapOf(1 to 4, 2 to 5, 3 to 6), buggyHash = buggyHash, buggyEquals = buggyEquals) }
    }

    @Test
    fun ok() {
        testEquality(defaultGroupSize = 2) {
            setup()
        }
    }

    @Test
    fun invalid_hash_code() {
        assertFailsWithMessage<AssertionError>("Map.hashCode") {
            testEquality(defaultGroupSize = 2) {
                setup(buggyHash = true)
            }
        }
    }

    @Test
    fun invalid_equals() {
        assertFailsWithMessage<AssertionError>("Map.equals") {
            testEquality(defaultGroupSize = 2) {
                setup(buggyEquals = true)
            }
        }
    }
}