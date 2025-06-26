package dev.adokky.eqtester

import kotlin.test.Test
import kotlin.test.assertFailsWith

class EqualityFailures: BaseTest() {
    @Test
    fun not_equal_objects_in_group() {
        assertFailsWith<AssertionError> {
            tester.testElements(ComparableBox(1), ComparableBox(2))
        }
    }

    @Test
    fun not_equal_ts_in_group() {
        class Buggy(v: Int, val ts: Int): BaseBox(v) {
            override fun toString() = ts.toString()
        }
        assertFailsWithMessage<AssertionError>("toString") {
            tester.test {
                group(Buggy(1, 0), Buggy(1, 0), Buggy(1, 1))
                checkToString = true
            }
        }
    }

    @Test
    fun equal_objects_in_groups() {
        assertFailsWith<AssertionError> {
            testEquality {
                group(2) { ComparableBox(1) }
                group(1) { ComparableBox(2) }
                group(1) { ComparableBox(1) }
                group(3) { ComparableBox(3) }
            }
        }
    }

    @Test
    fun equal_ts_in_groups() {
        class Buggy(v: Int, val ts: Int = v): BaseBox(v) {
            override fun toString() = ts.toString()
        }
        assertFailsWithMessage<AssertionError>("toString") {
            tester.test {
                group(Buggy(1), Buggy(1))
                group { Buggy(2) }
                group { Buggy(4) }
                group(Buggy(3), Buggy(3, ts = 2), Buggy(3))
                checkToString = true
            }
        }
    }

    @Test
    fun object_is_equal_to_everything() {
        @Suppress("EqualsOrHashCode")
        data class Buggy(val v: Any) {
            override fun equals(other: Any?): Boolean {
                if (other !is Buggy) return true
                return super.equals(other)
            }
        }
        assertFailsWith<AssertionError> {
            tester.testElements(Buggy(1), Buggy(1))
        }
    }

    @Test
    fun object_is_equal_to_null() {
        @Suppress("EqualsOrHashCode")
        data class Buggy(val v: Any) {
            override fun equals(other: Any?): Boolean {
                if (other == null) return true
                return super.equals(other)
            }
        }
        assertFailsWith<AssertionError> {
            tester.testElements(Buggy(1), Buggy(1))
        }
    }

    private class Hashed(val v: Any, val hash: Int = v.hashCode()) {
        override fun equals(other: Any?): Boolean {
            other as? Hashed ?: return false
            return other.v == v
        }
        override fun hashCode(): Int = hash
        override fun toString(): String = v.toString()
    }

    @Test
    fun hash_code_is_not_equal() {
        assertFailsWith<AssertionError> {
            tester.testElements(Hashed(1), Hashed(1), Hashed(1, hash = 177), Hashed(1))
        }
    }

    @Test
    fun hash_code_is_equal() {
        assertFailsWith<AssertionError> {
            tester.testElements(Hashed(1), Hashed(1), Hashed(1, hash = 177), Hashed(1))
        }
    }
}

