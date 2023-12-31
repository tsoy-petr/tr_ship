package com.example.demo.core.interactor

import com.example.demo.core.exception.Failure
import com.example.demo.core.functional.Either
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means that any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
abstract class UseCase<out Type, in Params>(
    private val backgroundContext: CoroutineContext = Dispatchers.IO,
    private val foregroundContext: CoroutineContext = Dispatchers.Default,
) where Type : Any {

    private var parentJob: Job = Job()

    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(
        params: Params,
        onResult: (Either<Failure, Type>) -> Unit = {},
    ) {

        GlobalScope.launch(foregroundContext) {
            val deferred = async(backgroundContext) {
                run(params)
            }
            onResult(deferred.await())
        }
    }

    fun unsubscribe() {
        parentJob.apply {
            cancelChildren()
            cancel()
        }
    }

    class None
}