package com.example.watchlist.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class CombinedLiveData<T1, T2, T3>(
    f1: LiveData<T1>,
    f2: LiveData<T2>,
    f3: LiveData<T3>
) : MediatorLiveData<Triple<T1?, T2?, T3?>>() {

    init {
        value = Triple(f1.value, f2.value, f3.value)

        addSource(f1) { t1: T1? ->
            val (_, t2, t3) = value!!
            value = Triple(t1, t2, t3)
        }

        addSource(f2) { t2: T2? ->
            val (t1, _, t3) = value!!
            value = Triple(t1, t2, t3)
        }

        addSource(f3) { t3: T3? ->
            val (t1, t2, _) = value!!
            value = Triple(t1, t2, t3)
        }
    }
}