package com.nguyen.unittest1

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Test
import org.junit.runner.RunWith
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * Tests that the parcelable interface is implemented correctly.
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class LogHistoryAndroidUnitTest {
    @Test
    fun logHistory_ParcelableWriteRead() {
        val originalLog = LogHistory2()

        // Set up the Parcelable object to send and receive.
        originalLog.addEntry(TEST_STRING, TEST_LONG)

        // Write the data
        val parcel = Parcel.obtain()
        originalLog.writeToParcel(parcel, originalLog.describeContents())

        // After you're done with writing, you need to reset the parcel for reading.
        parcel.setDataPosition(0)

        // Read the data
        val restoredLog = LogHistory2.createFromParcel(parcel)

        // Verify that the received data is correct.
        assert(restoredLog.data.size == 1)
        assert(restoredLog.data[0].first == TEST_STRING)
        assert(restoredLog.data[0].second == TEST_LONG)
    }

    @Test
    fun logHistory_SerializableWriteRead() {
        val originalLog = LogHistory()

        // Set up the Parcelable object to send and receive.
        originalLog.addEntry(TEST_STRING, TEST_LONG)

        // Write the data
        // this will cause java.io.FileNotFoundException: loghistory.txt: open failed: EROFS (Read-only file system)
        // there's ways to write to Android OS, but I'll avoid fixing the error for now.
        val fileOutputStream = FileOutputStream("loghistory.txt")
        val objectOutputStream = ObjectOutputStream(fileOutputStream)
        objectOutputStream.writeObject(originalLog)
        objectOutputStream.flush()
        objectOutputStream.close()

        // Read the data
        val fileInputStream = FileInputStream("loghistory.txt")
        val objectInputStream = ObjectInputStream(fileInputStream)
        val restoredLog = objectInputStream.readObject() as LogHistory

        // Verify that the received data is correct.
        assert(restoredLog.data.size == 1)
        assert(restoredLog.data[0].first == TEST_STRING)
        assert(restoredLog.data[0].second == TEST_LONG)
    }

    companion object {
        const val TEST_STRING = "This is a string"
        const val TEST_LONG = 12345678L
    }
}