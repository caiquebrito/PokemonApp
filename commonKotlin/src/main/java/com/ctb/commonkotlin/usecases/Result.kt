package com.ctb.commonkotlin.usecases

import com.ctb.commonkotlin.usecases.Result.Companion.fromNullable

/**
 * A simple wrapper representing the outcome of an operation.
 *
 * This sealed type models success and failure without throwing exceptions
 * through the regular call stack. Use it when you want to return either a
 * successful value ([Result.Success]) or an error that can be handled by the caller
 * ([Result.Failure]).
 *
 * The companion helper [fromNullable] is convenient when converting nullable
 * results into a [Result], treating `null` as a failure.
 */
sealed class Result<out TResultModel> {
    companion object {
        /**
         * Convert a nullable value into a [Result].
         * Returns [Result.Success] when [result] is non-null, otherwise returns [Result.Failure].
         */
        fun <TResultModel> fromNullable(result: TResultModel?) =
            when (result) {
                null -> Failure()
                else -> Success(result)
            }
    }

    /**
     * Represents a successful operation and carries the resulting value.
     * @param result the successful result value
     */
    data class Success<out TResultModel>(
        val result: TResultModel,
    ) : Result<TResultModel>()

    /**
     * Represents a failed operation and carries an optional error/exception.
     * @param error the error that caused the failure, or `null` if unknown
     */
    data class Failure(
        val error: Throwable? = null,
    ) : Result<Nothing>()
}

/**
 * Map a successful [Result] with [callback], returning a new [Result] whose
 * success value is the result of the callback. Any exception thrown by the
 * callback will be captured and returned as [Result.Failure].
 */
@Suppress("TooGenericExceptionCaught") // Any callback failure is intentionally captured as Result.Failure.
inline fun <TResultModel, MappedResult> Result<TResultModel>.onSuccess(
    callback: (TResultModel) -> (MappedResult),
): Result<MappedResult> =
    when (this) {
        is Result.Success ->
            try {
                Result.Success(callback.invoke(this.result))
            } catch (e: Exception) {
                Result.Failure(e)
            }

        is Result.Failure -> this
    }

/**
 * Invoke [callback] if this [Result] is a [Result.Failure]. Returns the original
 * result unchanged so calls can be chained.
 */
inline fun <TResultModel> Result<TResultModel>.onFailure(
    callback: (Throwable) -> Unit,
): Result<TResultModel> {
    if (this is Result.Failure) callback(this.error ?: UnknownException())
    return this
}
