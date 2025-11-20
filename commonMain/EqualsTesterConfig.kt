package dev.adokky.eqtester

/**
 * Configuration interface for [EqualsTester].
 *
 * Defines behavior and constraints for equality testing, including group validation,
 * performance limits, and additional contract checks.
 *
 * Instances are created via [EqualsTesterConfigBuilder] using the [testEquality] entry point.
 *
 * @see EqualsTester
 * @see EqualsTesterConfigBuilder
 * @see testEquality
 */
interface EqualsTesterConfig {
    /**
     * If enabled, the tester will throw an exception if any two objects within the same group
     * are identical (reference-equal).
     *
     * Default: `false`.
     *
     * Useful for ensuring that `equals()` is tested with distinct but equivalent instances.
     */
    val requireNonIdentical: Boolean

    /**
     * Maximum number of test iterations before switching to random sampling.
     *
     * Default: `1_000_000`.
     *
     * Large equality groups or many groups may result in a combinatorial explosion of checks.
     * To prevent performance issues, if the number of combinations exceeds this limit,
     * the tester will perform randomized checks instead of exhaustive ones.
     *
     * Lower this value if tests are too slow.
     *
     * @see EqualsTesterConfigBuilder.maxIterations
     */
    val maxIterations: Int

    /**
     * Whether to verify that all objects within a group have the same `toString()` result.
     *
     * Default: `false`.
     */
    val checkToString: Boolean

    /**
     * Whether to perform additional contract checks for collections like [List], [Set], and [Map].
     * When enabled, the tester validates that `hashCode()` and `equals()` conform to the standard
     * collection contracts (e.g., list equality based on element order,
     * collection-specific hash code formulas).
     *
     * Default: `true`.
     */
    val collectionContractCheck: Boolean

    /**
     * The default number of instances to generate per group when using factory-based group definitions.
     *
     * Default: `2`.
     *
     * Used by [EqualsTesterConfigBuilder.group] when no explicit size is provided.
     *
     * Example:
     * ```
     * testEquality {
     *     defaultGroupSize = 3
     *     group { User("alice") } // generates 3 instances
     * }
     * ```
     * @see EqualsTesterConfigBuilder.group
     */
    val defaultGroupSize: Int
}