package dev.adokky.eqtester

import kotlin.test.Test
import kotlin.test.assertFailsWith

class GroupTestPreValidation: BaseTest() {
    @Test
    fun empty_groups() {
        assertFailsWith<IllegalArgumentException> {
            tester.test(EqualsTesterConfigImpl(listOf()))
        }
    }

    @Test
    fun empty_group() {
        assertFailsWith<IllegalArgumentException> {
            tester.test(EqualsTesterConfigImpl(listOf(listOf(1), listOf())))
        }
    }

    @Test
    fun identical_objects_not_allowed() {
        assertFailsWith<IllegalArgumentException> {
            testEquality {
                requireNonIdentical = true
                val box = ComparableBox(3)
                group(
                    ComparableBox(3),
                    ComparableBox(3),
                    box,
                    box,
                    ComparableBox(3)
                )
            }
        }
    }

    @Test
    fun identical_objects_allowed() {
        val box = ComparableBox(3)
        tester.test(
            EqualsTesterConfigImpl(
                groups = listOf(listOf(ComparableBox(3), ComparableBox(3), box, box, ComparableBox(3))),
                requireNonIdentical = false
            )
        )
    }
}

