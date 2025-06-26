package dev.adokky.eqtester

abstract class BaseTest {
    protected fun <T: Any> test(vararg values: T) =
        SingleGroupChecker().test(EqualsTesterConfigImpl(listOf(values.toList()), checkToString = true), groupIndex = 0)

    protected val tester = EqualsTester()
}