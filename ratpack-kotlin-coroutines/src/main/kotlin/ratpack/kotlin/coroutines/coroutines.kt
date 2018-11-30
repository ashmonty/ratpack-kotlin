/**
 *  All credit goes to https://github.com/gregopet for inspiration!
 */

package ratpack.kotlin.coroutines

import kotlinx.coroutines.AbstractCoroutine
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import kotlinx.coroutines.suspendCancellableCoroutine
import ratpack.exec.Blocking
import ratpack.exec.Downstream
import ratpack.exec.Promise
import ratpack.handling.Context
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *
 * Runs the given block on the request thread.
 *
 * Frees the request thread as soon as the first <tt>await<tt> (or any other suspendable function) is encountered and
 * continues work on another appropriate thread after the operation finishes.
 *
 * @param block The block to execute
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun Context.async(block: suspend () -> Any?) {
  GlobalScope.launch(Unconfined, CoroutineStart.UNDISPATCHED) {
    try {
      block()
    } catch (t: Throwable) {
      this@async.error(t)
    }
  }
}

/**
 * Returns the result of the [block] as soon as the blocking computation completes. The request thread is released
 * during the blocking operation.
 */
suspend fun <T> await(block: () -> T): T = Blocking.get(block).await()

/**
 * Resolves the promise and returns its value as soon as the blocking computation completes. The request thread is released
 * during the blocking operation.
 */
suspend fun <T> Promise<T>.await(fork: Boolean = false): T = suspendCancellableCoroutine { cont: CancellableContinuation<T> ->
  if (fork) { this.fork() } else { this }.onError { cont.resumeWithException(it) }.then { cont.resume(it) }
}

/**
 * Convert this [Promise] into a [Deferred] by launching an async coroutine, inside of which
 * the promised value will be resolved.
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun <T> Promise<T>.defer(fork: Boolean = false): Deferred<T> {
  return GlobalScope.async(Unconfined, CoroutineStart.UNDISPATCHED) {
    this@defer.await(fork)
  }
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun <T1, T2, R> zip(p1: Promise<T1>, p2: Promise<T2>, zipper: (T1, T2) -> R): R {
  val d1 = p1.defer(true)
  val d2 = p2.defer(true)
  return zipper(d1.await(), d2.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun <T1, T2, T3, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                zipper: (T1, T2, T3) -> R): R {
  val d1 = p1.defer(true)
  val d2 = p2.defer(true)
  val d3 = p3.defer(true)
  return zipper(d1.await(), d2.await(), d3.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun <T1, T2, T3, T4, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                    p4: Promise<T4>, zipper: (T1, T2, T3, T4) -> R): R {
  val d1 = p1.defer(true)
  val d2 = p2.defer(true)
  val d3 = p3.defer(true)
  val d4 = p4.defer(true)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun <T1, T2, T3, T4, T5, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                        p4: Promise<T4>, p5: Promise<T5>, zipper: (T1, T2, T3, T4, T5) -> R): R {
  val d1 = p1.defer(true)
  val d2 = p2.defer(true)
  val d3 = p3.defer(true)
  val d4 = p4.defer(true)
  val d5 = p5.defer(true)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await(), d5.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun <T1, T2, T3, T4, T5, T6, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                            p4: Promise<T4>, p5: Promise<T5>, p6: Promise<T6>,
                                            zipper: (T1, T2, T3, T4, T5, T6) -> R): R {
  val d1 = p1.defer(true)
  val d2 = p2.defer(true)
  val d3 = p3.defer(true)
  val d4 = p4.defer(true)
  val d5 = p5.defer(true)
  val d6 = p6.defer(true)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await(), d5.await(), d6.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun <T1, T2, T3, T4, T5, T6, T7, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                                p4: Promise<T4>, p5: Promise<T5>, p6: Promise<T6>,
                                                p7: Promise<T7>, zipper: (T1, T2, T3, T4, T5, T6, T7) -> R): R {
  val d1 = p1.defer(true)
  val d2 = p2.defer(true)
  val d3 = p3.defer(true)
  val d4 = p4.defer(true)
  val d5 = p5.defer(true)
  val d6 = p6.defer(true)
  val d7 = p7.defer(true)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await(), d5.await(), d6.await(), d7.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun <T1, T2, T3, T4, T5, T6, T7, T8, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                                    p4: Promise<T4>, p5: Promise<T5>, p6: Promise<T6>,
                                                    p7: Promise<T7>, p8: Promise<T8>, zipper: (T1, T2, T3, T4, T5, T6, T7, T8) -> R): R {
  val d1 = p1.defer(true)
  val d2 = p2.defer(true)
  val d3 = p3.defer(true)
  val d4 = p4.defer(true)
  val d5 = p5.defer(true)
  val d6 = p6.defer(true)
  val d7 = p7.defer(true)
  val d8 = p8.defer(true)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await(), d5.await(), d6.await(), d7.await(), d8.await())
}

/**
 * Consume the promises in parallel and execute the zipper function on the results.
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> zip(p1: Promise<T1>, p2: Promise<T2>, p3: Promise<T3>,
                                                        p4: Promise<T4>, p5: Promise<T5>, p6: Promise<T6>,
                                                        p7: Promise<T7>, p8: Promise<T8>, p9: Promise<T9>,
                                                        zipper: (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R): R {
  val d1 = p1.defer(true)
  val d2 = p2.defer(true)
  val d3 = p3.defer(true)
  val d4 = p4.defer(true)
  val d5 = p5.defer(true)
  val d6 = p6.defer(true)
  val d7 = p7.defer(true)
  val d8 = p8.defer(true)
  val d9 = p9.defer(true)
  return zipper(d1.await(), d2.await(), d3.await(), d4.await(), d5.await(), d6.await(), d7.await(), d8.await(), d9.await())
}

/**
 * Creates a [promise][Promise] that will run a given [block] in a coroutine.
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.InternalCoroutinesApi
fun <T : Any> CoroutineScope.promise(
  context: CoroutineContext = Unconfined,
  block: suspend CoroutineScope.() -> T
): Promise<T> = Promise.async { downstream ->
  val newContext = newCoroutineContext(context)
  val coroutine = PromiseCoroutine(newContext, downstream)
  coroutine.start(CoroutineStart.UNDISPATCHED, coroutine, block)
}

@kotlinx.coroutines.InternalCoroutinesApi
private class PromiseCoroutine<T>(
  parentContext: CoroutineContext,
  private val downstream: Downstream<T>
) : AbstractCoroutine<T>(parentContext, true) {
  override val cancelsParent: Boolean get() = true
  override fun onCompleted(value: T) {
    downstream.success(value)
  }

  override fun onCompletedExceptionally(exception: Throwable) {
    downstream.error(exception)
  }
}





