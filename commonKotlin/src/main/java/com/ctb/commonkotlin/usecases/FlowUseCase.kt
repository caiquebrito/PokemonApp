package com.ctb.commonkotlin.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

/**
 * Base class for use-cases that return a cold Flow of values.
 * Errors thrown during collection are converted into [Result.Failure] via [kotlinx.coroutines.flow.catch].
 * The caller (for example a ViewModel) can convert the returned Flow into a
 * StateFlow with [kotlinx.coroutines.flow.stateIn] if it needs to share/cache the last emission.
 */
abstract class FlowUseCase<in TParam, out TResult>(
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(param: TParam? = null) =
        performAction(param)
            .catch { exception -> emit(Result.Failure(exception)) }
            .flowOn(dispatcher)

    /**
     * Produce a cold Flow of TResult. Keep this function non-suspending so errors
     * that happen during collection are caught by the upstream `catch` above.
     */
    protected abstract suspend fun performAction(param: TParam?): Flow<Result<TResult>>
}
