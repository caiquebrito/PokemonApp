package com.ctb.commonkotlin.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals

/**
 * Class to encapsulate and make easier to handle flows when testing it
 * That class will launch a job and collect all values emitted on a specific flow and scope passed throught constructor
 * @param scope A coroutine scope to launch a new job blocking
 * @param testScheduler A testing scheduler to providing delay-skipping behavior
 * @param flow The flow param to be used as observer, it will cache every emission
 *
 * IMPORTANT: remember to always finish a job to avoid "timeout 60s" await time
 *
 * */
@OptIn(ExperimentalCoroutinesApi::class)
class TestFlowCollector<T>(
    scope: CoroutineScope,
    testScheduler: TestCoroutineScheduler,
    flow: Flow<T>,
) {
    private val values = mutableListOf<T>()

    private val job: Job =
        scope.launch(UnconfinedTestDispatcher(testScheduler)) {
            flow.collect { values.add(it) }
        }

    fun assertNoValues() {
        assertEquals(emptyList<T>(), this.values)
    }

    fun assertValuesInOrder(vararg values: T) {
        assertEquals(values.toList(), this.values)
    }

    fun finishJob() {
        job.cancel()
    }
}
