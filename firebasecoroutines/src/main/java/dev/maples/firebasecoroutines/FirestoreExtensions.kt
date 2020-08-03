package dev.maples.firebasecoroutines

import android.util.Log
import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.firebase.firestore.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

const val TAG = "Flowbase/FirestoreExtensions"

@Keep
@ExperimentalCoroutinesApi
fun <T> CollectionReference.addBackgroundListener(mapper: (QuerySnapshot?) -> T): Flow<T> {
    return callbackFlow {
        val listenerRegistration =
            addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    Log.w(TAG, exception)
                    return@addSnapshotListener
                }
                offer(querySnapshot)
            }
        awaitClose {
            Log.d(TAG, "cancelling the listener on collection")
            listenerRegistration.remove()
        }
    }.map {
        return@map mapper(it)
    }
}

@Keep
@ExperimentalCoroutinesApi
fun <T> Query.addBackgroundListener(mapper: (QuerySnapshot?) -> T): Flow<T> {
    return callbackFlow {
        val listenerRegistration =
            addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    Log.w(TAG, exception)
                    return@addSnapshotListener
                }
                offer(querySnapshot)
            }
        awaitClose {
            Log.d(TAG, "cancelling the listener on collection")
            listenerRegistration.remove()
        }
    }.map {
        return@map mapper(it)
    }
}

@Keep
@ExperimentalCoroutinesApi
fun <T> DocumentReference.addBackgroundListener(mapper: (DocumentSnapshot?) -> T): Flow<T> {
    return callbackFlow {
        val listenerRegistration =
            addSnapshotListener { docSnapshot, exception ->
                if (exception != null) {
                    Log.w(TAG, exception)
                    return@addSnapshotListener
                }
                offer(docSnapshot)
            }
        awaitClose {
            Log.d(TAG, "cancelling the listener on collection")
            listenerRegistration.remove()
        }
    }.map {
        return@map mapper(it)
    }
}

@Keep
fun <T> Flow<T>.getLiveDataFromFlow(): LiveData<T> {
    return liveData(Dispatchers.IO) {
        collect {
            emit(it)
        }
    }
}