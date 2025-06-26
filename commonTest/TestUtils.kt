package dev.adokky.eqtester

import kotlin.test.assertFailsWith
import kotlin.test.fail

inline fun <reified T: Throwable> assertFailsWithMessage(fragment: String, body: () -> Unit) {
    val ex = assertFailsWith<T> { body() }
    val msg = ex.message ?: fail("empty error message")
    if (fragment !in msg) fail(
        "Expected error message to have:\n" +
        "${fragment.prependIndent("    ")}\n" +
        "But actual message is:\n" +
        msg.prependIndent("    ")
    )
}