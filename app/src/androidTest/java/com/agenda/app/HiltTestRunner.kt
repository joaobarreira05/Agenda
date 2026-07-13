package com.agenda.app

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Custom test runner that uses [HiltTestApplication] instead of
 * [AgendaApplication] for instrumented tests.
 *
 * This allows Hilt to provide test-scoped dependencies without
 * initializing the full application context.
 *
 * Referenced in app/build.gradle.kts as testInstrumentationRunner.
 */
class HiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
