@file:Suppress("unused")

package com.ctb.commonkotlin.test

import com.ctb.commonkotlin.usecases.FlowUseCase
import com.ctb.commonkotlin.usecases.Result
import io.mockk.coEvery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.fail

/**
 * Utility test extensions and helpers for unit testing coroutines-based flows and use-cases.
 *
 * This file provides:
 * - helpers to stub `FlowUseCase` executions with success/failure results using MockK,
 * - helpers to collect and assert emissions from `StateFlow` and `SharedFlow` inside `TestScope`.
 *
 * The helpers are intentionally lightweight and tailored to the project's `Result` wrapper
 * and `FlowUseCase` contract.
 */

inline fun <reified TParam, TResult> FlowUseCase<TParam, TResult>.mockSuccess(
    result: TResult,
    onParam: TParam? = null,
) = coEvery {
    this@mockSuccess.invoke(
        param = onParam ?: any(),
    )
} returns flow { emit(Result.Success(result)) }

inline fun <reified TParam, TResult> FlowUseCase<TParam, TResult>.mockFailure(
    error: Throwable? = null,
    onParam: TParam? = null,
) = coEvery {
    this@mockFailure.invoke(
        param = onParam ?: any(),
    )
} returns flow { emit(Result.Failure(error)) }

/**
 * Start collecting [Flow] emissions inside the given [TestScope].
 *
 * Returns a [TestFlowCollector] which can be used to assert collected values and
 * must be finished by calling [TestFlowCollector.finishJob] when assertions are done.
 *
 * @param scope the test scope used to launch observation jobs and control virtual time
 */
fun <T> Flow<T>.test(scope: TestScope): TestFlowCollector<T> = TestFlowCollector(scope, scope.testScheduler, this)

/**
 * Observe a [StateFlow] and a [SharedFlow] concurrently inside this [TestScope].
 *
 * The provided [block] receives two [TestFlowCollector] instances (state, shared) and is
 * expected to drive the unit under test (for example, call ViewModel functions). After [block]
 * returns both collectors are finished automatically.
 *
 * Sample usage:
 * ```kotlin
 * fun myTest() = runTest {
 *      val entity = Entity(something...)
 *      val uiState = UiState(showLoading = true)
 *      collectTestFlows(viewModel.state, viewModel.effect) { state, effect ->
 *          viewModel.callFunction()
 *          state.assertValuesInOrder(
 *              uiState,
 *              uiState.copy(showLoading = false, objectReturn = entity)
 *          )
 *          effect.assertNoValues()
 *     }
 * }
 * ```
 *
 * @param state the [StateFlow] to observe
 * @param shared the [SharedFlow] to observe
 * @param block invoked with the created collectors while the test logic runs
 */
fun <T> TestScope.collectTestFlows(
    state: StateFlow<T>,
    shared: SharedFlow<T>,
    block: (TestFlowCollector<T>, TestFlowCollector<T>) -> Unit,
) {
    val stateObserver = state.test(this)
    val sharedObserver = shared.test(this)
    block.invoke(stateObserver, sharedObserver)
    stateObserver.finishJob()
    sharedObserver.finishJob()
}

/**
 * Observe a single [StateFlow] inside this [TestScope].
 *
 * The provided [block] receives a [TestFlowCollector] which can be used to assert
 * collected values. The collector is finished automatically after [block] returns.
 *
 * Sample usage:
 * ```kotlin
 * fun myTest() = runTest {
 *      val entity = Entity(something...)
 *      val uiState = UiState(showLoading = true)
 *      collectTestFlows(viewModel.state) { state ->
 *          viewModel.callFunction()
 *          state.assertValuesInOrder(
 *              UiState(showLoading = true)
 *              UiState(showLoading = false, object = entity)
 *          )
 *     }
 * }
 * ```
 */
fun <T> TestScope.collectTestFlows(
    state: StateFlow<T>,
    block: (TestFlowCollector<T>) -> Unit,
) {
    val stateObserver = state.test(this)
    block.invoke(stateObserver)
    stateObserver.finishJob()
}

/**
 * Observe a single [SharedFlow] inside this [TestScope].
 *
 * The provided [block] receives a [TestFlowCollector] which can be used to assert
 * collected values. The collector is finished automatically after [block] returns.
 *
 * Sample usage:
 * ```kotlin
 * fun myTest() = runTest {
 *      collectTestFlows(viewModel.effect) { effect ->
 *          viewModel.callFunction()
 *          effect.assertValuesInOrder(
 *              UiState.ShowError
 *          )
 *     }
 * }
 * ```
 */
fun <T> TestScope.collectTestFlows(
    shared: SharedFlow<T>,
    block: (TestFlowCollector<T>) -> Unit,
) {
    val sharedObserver = shared.test(this)
    block.invoke(sharedObserver)
    sharedObserver.finishJob()
}

/**
 * Assert that this [Flow] of [Result] emits at least one [Result.Failure].
 *
 * Use from a coroutine test scope (for example inside `runTest`) to verify the
 * failure case without repeating boilerplate collection code.
 */
@Suppress("SwallowedException") // The absence of the element IS the assertion failure.
suspend fun <T> Flow<Result<T>>.assertEmitsFailure() {
    try {
        this.first { it is Result.Failure }
    } catch (ex: NoSuchElementException) {
        fail("Expected flow to emit Result.Failure but it did not.")
    }
}

/**
 * Assert that this [Flow] of [Result] emits at least one [Result.Success].
 *
 * Useful to mirror `assertEmitsFailure` in tests.
 */
@Suppress("SwallowedException") // The absence of the element IS the assertion failure.
suspend fun <T> Flow<Result<T>>.assertEmitsSuccess() {
    try {
        this.first { it is Result.Success }
    } catch (ex: NoSuchElementException) {
        fail("Expected flow to emit Result.Success but it did not.")
    }
}
