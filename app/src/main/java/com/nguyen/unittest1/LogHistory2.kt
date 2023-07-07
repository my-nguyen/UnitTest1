package com.nguyen.unittest1

import android.os.Parcel
import android.os.Parcelable
import android.util.Pair

/**
 * Model that stores a parcelable log of entries, each with a timestamp.
 */
class LogHistory2() : Parcelable {
    // Used to store the data to be used by the activity.
    private val mData: MutableList<Pair<String?, Long>> = ArrayList()

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        // Prepare an array of strings and an array of timestamps.
        val texts = arrayOfNulls<String>(mData.size)
        val timestamps = LongArray(mData.size)

        // Store the data in the arrays.
        for (i in mData.indices) {
            texts[i] = mData[i].first
            timestamps[i] = mData[i].second
        }
        // Write the size of the arrays first.
        out.writeInt(texts.size)

        // Write the two arrays in a specific order.
        out.writeStringArray(texts)
        out.writeLongArray(timestamps)
    }

    /**
     * Returns a copy of the current data used by the activity.
     */
    val data: List<Pair<String?, Long>>
        get() = ArrayList(mData)

    /**
     * Adds a new entry to the log.
     * @param text the text to be stored in the log
     * @param timestamp the current time in milliseconds since January 1, 1970 00:00:00.0 UTC.
     */
    fun addEntry(text: String?, timestamp: Long) {
        mData.add(Pair(text, timestamp))
    }

    // Constructor used from the CREATOR field that unpacks a Parcel.
    constructor(`in`: Parcel): this() {
        // First, read the size of the arrays that contain the data.
        val length = `in`.readInt()

        // Create the arrays to store the data.
        val texts = arrayOfNulls<String>(length)
        val timestamps = LongArray(length)

        // Read the arrays in a specific order.
        `in`.readStringArray(texts)
        `in`.readLongArray(timestamps)

        // The lengths of both arrays should match or the data is corrupted.
        check(texts.size == timestamps.size) { "Error reading from saved state." }

        // Reset the data container and update the data.
        mData.clear()
        for (i in texts.indices) {
            val pair = Pair(texts[i], timestamps[i])
            mData.add(pair)
        }
    }

    companion object CREATOR : Parcelable.Creator<LogHistory2> {
        override fun createFromParcel(parcel: Parcel): LogHistory2 {
            return LogHistory2(parcel)
        }

        override fun newArray(size: Int): Array<LogHistory2?> {
            return arrayOfNulls(size)
        }
    }
}