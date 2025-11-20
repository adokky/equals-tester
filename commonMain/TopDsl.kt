package dev.adokky.eqtester

/**
 * Runs equality tests using the provided DSL configuration.
 *
 * This is the primary entry point for defining and executing equality contract tests.
 * It configures groups of objects that should be equal (or not equal to each other),
 * and verifies that their `equals`, `hashCode`, and optionally `toString` implementations
 * adhere to the general contracts.
 *
 * Example usage:
 * ```
 * testEquality {
 *     group { User("Alice", 30) }
 *     group { User("Bob", 25) }
 *     checkToString = true
 * }
 * ```
 *
 * The DSL allows specifying multiple groups of elements that are expected to be equal within each group,
 * but not equal to elements in other groups.
 *
 * @throws AssertionError if any of the equality contract rules are violated.
 */
fun testEquality(builder: EqualsTesterConfigBuilder.() -> Unit) {
    EqualsTester().test(builder)
}

/**
 * Configures and runs equality tests on this [EqualsTester] instance using a DSL builder.
 *
 * @param builder A DSL block for configuring test groups and options.
 */
fun EqualsTester.test(builder: EqualsTesterConfigBuilder.() -> Unit) {
    test(
        EqualsTesterConfigBuilder()
            .apply(builder)
            .toConfig()
    )
}

