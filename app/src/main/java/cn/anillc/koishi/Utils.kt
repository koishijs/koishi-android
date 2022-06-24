package cn.anillc.koishi

import io.vertx.core.Future
import io.vertx.core.Vertx
import java.lang.Exception

fun <T> Vertx.execute(block: () -> T): Future<T> {
    return executeBlocking<T> {
        try {
            it.complete(block())
        } catch (e: Exception) {
            it.fail(e)
        }
    }
}