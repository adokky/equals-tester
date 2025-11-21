package dev.adokky.eqtester

import kotlin.test.Test

class EqualityFailures: BaseTest() {
    @Test
    fun not_equal_objects_in_group() {
        assertFailsWithMessage<AssertionError>("elements at index=0 and index=1 are not equal") {
            tester.testElements(Hashed(1), Hashed(2, hash = 1))
        }
    }

    @Test
    fun dirty_hash_code() {
        data class Dirty(val v: Int) {
            var hashCode = v
            override fun equals(other: Any?) = v.equals(other)
            override fun hashCode() = hashCode++
        }

        assertFailsWithMessage<AssertionError>("element at index=0 has different hashCode() results on subsequent calls: 1 != 2") {
            tester.testElements(Dirty(1), Dirty(1))
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
        assertFailsWithMessage<AssertionError>("elements at index=0 of group=0 and index=0 of group='xyz' are equal") {
            testEquality {
                groupOfSize(2) { ComparableBox(1) }
                groupOfSize(1) { ComparableBox(2) }
                namedGroup("xyz", 1) { ComparableBox(1) }
                namedGroup("abc", ComparableBox(3), ComparableBox(3), ComparableBox(3))
            }
        }
    }

    @Test
    fun equal_ts_in_groups() {
        class Buggy(v: Int, val ts: Int = v): BaseBox(v) {
            override fun toString() = ts.toString()
        }
        fun EqualsTesterConfigBuilder.setup() {
            group(Buggy(1), Buggy(1))
            group { Buggy(2) }
            group { Buggy(4) }
            group(Buggy(3), Buggy(3, ts = 2), Buggy(3))
        }
        assertFailsWithMessage<AssertionError>("toString") {
            tester.test {
                setup()
                checkToString = true
            }
        }
        tester.test {
            setup()
            checkToString = false
        }
    }

    @Test
    fun object_is_equal_to_everything() {
        @Suppress("EqualsOrHashCode")
        data class Buggy(val v: Any) {
            override fun equals(other: Any?): Boolean {
                if (other != null && other !is Buggy) return true
                return super.equals(other)
            }
        }
        assertFailsWithMessage<AssertionError>("element at index=0 equals any unknown type") {
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
        assertFailsWithMessage<AssertionError>("element at index=0 equals to null") {
            tester.testElements(Buggy(1), Buggy(1))
        }
    }

    private class Hashed(val v: Any, val hash: Int = v.hashCode()) {
        override fun equals(other: Any?): Boolean {
            if (other !is Hashed) return false
            return other.v == v
        }
        override fun hashCode(): Int = hash
        override fun toString(): String = v.toString()
    }

    @Test
    fun hash_code_is_not_equal() {
        assertFailsWithMessage<AssertionError>("elements at index=0 and index=2 have different hash code") {
            tester.testElements(Hashed(1), Hashed(1), Hashed(1, hash = 177), Hashed(1))
        }
    }
}

