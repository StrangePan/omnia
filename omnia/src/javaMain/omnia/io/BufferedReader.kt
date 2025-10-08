package omnia.io

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.observable.observableUsing
import java.io.BufferedReader
import omnia.io.filesystem.FileNotFoundException

fun BufferedReader.asObservable(): Observable<String> =
  observableUsing(
    resourceSupplier = { this },
    resourceCleanup = { it.close() },
    eager = true) { reader: BufferedReader ->
      observable { emitter ->
        while (!emitter.isDisposed) {
          try {
            emitter.onNext(reader.readLine() ?: break)
          } catch (e: java.io.FileNotFoundException) {
            throw FileNotFoundException(e)
          } catch (e: java.io.IOException) {
            throw IOException(e)
          }
        }
        if (!emitter.isDisposed) {
          emitter.onComplete()
        }
      }
    }
