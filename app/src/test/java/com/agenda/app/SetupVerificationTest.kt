package com.agenda.app

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Dummy test to verify that the JUnit 5 test setup is working correctly.
 *
 * This test should be removed once real tests are added.
 */
@DisplayName("Project Setup Verification")
class SetupVerificationTest {

    @Test
    @DisplayName("JUnit 5 is correctly configured")
    fun `junit5 setup works`() {
        assertTrue(true, "JUnit 5 is working correctly")
    }

    @Test
    @DisplayName("Kotlin version is compatible")
    fun `kotlin version is compatible`() {
        val kotlinVersion = KotlinVersion.CURRENT
        assertTrue(
            kotlinVersion.major >= 2,
            "Expected Kotlin 2.x but got $kotlinVersion"
        )
    }
}
