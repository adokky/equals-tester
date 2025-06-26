package dev.adokky.eqtester

fun EqualsTester.test(
    defaultGroupSize: Int = EqualsTesterConfigBuilder.DEFAULT_GROUP_SIZE,
    builder: EqualsTesterConfigBuilder.() -> Unit
) {
    test(
        EqualsTesterConfigBuilder(defaultGroupSize = defaultGroupSize)
            .apply(builder)
            .toConfig()
    )
}

fun testEquality(
    defaultGroupSize: Int = EqualsTesterConfigBuilder.DEFAULT_GROUP_SIZE,
    builder: EqualsTesterConfigBuilder.() -> Unit
) {
    EqualsTester().test(defaultGroupSize = defaultGroupSize, builder)
}