package com.example.watchlist

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.watchlist.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> LiveData<T>.debounce(duration: Long = 1000L, coroutineScope: CoroutineScope) = MediatorLiveData<T>().also { mediator ->

    val source = this
    var job: Job? = null

    mediator.addSource(source) {
        job?.cancel()
        job = coroutineScope.launch {
            delay(duration)
            mediator.value = source.value
        }
    }
}

fun Fragment.setToolbarTitle(@StringRes resId: Int) {
    (activity as? MainActivity)?.supportActionBar?.title = getString(resId)
}