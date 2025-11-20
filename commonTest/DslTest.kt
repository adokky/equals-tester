package dev.adokky.eqtester

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DslTest {
    @Test
    fun build_config() {
        for (tsc in arrayOf(true, false))
        for (rid in arrayOf(true, false)) {
            val cfg = EqualsTesterConfigBuilder().apply {
                group { 1 }
                groupOfSize(5) { 2 }
                group { 3 }
                group(4, 4, 4, 4, 4)
                defaultGroupSize = 3
                requireNonIdentical = rid
                checkToString = tsc
                maxIterations = 343
            }.toConfig()

            assertEquals(343, cfg.maxIterations)
            assertEquals(rid, cfg.requireNonIdentical)
            assertEquals(tsc, cfg.checkToString)
            assertEquals(
                listOf(
                    TestGroup(1, 1, 1),
                    TestGroup(2, 2, 2, 2, 2),
                    TestGroup(3, 3, 3),
                    TestGroup(4, 4, 4, 4, 4),
                ),
                cfg.groups
            )
        }
    }

    @Test
    fun config_default_group_size() {
        val cfg = EqualsTesterConfigBuilder().apply {
            group { 1 }
            groupOfSize(5) { 2 }
            defaultGroupSize = 4
        }.toConfig()
        assertEquals(
            listOf(
                TestGroup(1, 1, 1, 1),
                TestGroup(2, 2, 2, 2, 2),
            ),
            cfg.groups
        )
    }

    @Test
    fun test_success() {
        testEquality {
            requireNonIdentical = false
            group { 1 }
            groupOfSize(2) { 2 }
            groupOfSize(5) { 3 }
        }
    }

    @Test
    fun test_fail_1() {
        assertFailsWith<AssertionError> {
            testEquality {
                requireNonIdentical = false
                group(listOf(1, 2, 1))
                group { 2 }
            }
        }
    }

    @Test
    fun test_fail_2() {
        assertFailsWith<AssertionError> {
            testEquality {
                requireNonIdentical = false
                group { 2 }
                group { 2 }
            }
        }
    }
}