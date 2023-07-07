package com.nguyen.unittest1

import android.util.Pair
import java.io.Serializable

class LogHistory : Serializable {
    // Used to store the data to be used by the activity.
    private val _data: MutableList<Pair<String?, Long>> = ArrayList()
    val data: List<Pair<String?, Long>>
        get() = ArrayList(_data)

    fun addEntry(text: String?, timestamp: Long) {
        _data.add(Pair(text, timestamp))
    }
}