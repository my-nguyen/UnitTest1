package com.nguyen.unittest1

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.nguyen.unittest1.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val KEY_HISTORY_DATA = "KEY_HISTORY_DATA"

/**
 * Lets the user add lines to a multi-line log. When the device is rotated, the state is saved
 * and restored.
 */
class MainActivity : Activity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var logHistory: LogHistory
    private var isHistoryEmpty = true

    /**
     * Called when the user wants to append an entry to the history.
     */
    fun updateHistory(view: View) {
        val timestamp = System.currentTimeMillis()

        // Show it back to the user.
        appendEntryToView(binding.editText.text.toString(), timestamp)

        // Update the history.
        logHistory.addEntry(binding.editText.text.toString(), timestamp)

        // Reset the EditText.
        binding.editText.setText("")
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logHistory = LogHistory()
        if (savedInstanceState != null) {
            // We've got a past state, apply it to the UI.
            logHistory = savedInstanceState.getSerializable(KEY_HISTORY_DATA, LogHistory::class.java)!!
            for (entry in logHistory.data) {
                appendEntryToView(entry.first, entry.second!!)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_HISTORY_DATA, logHistory)
    }

    private fun appendEntryToView(text: String?, timestamp: Long) {
        val date = Date(timestamp)
        // Add a newline if needed or clear the text view (to get rid of the hint).
        if (!isHistoryEmpty) {
            binding.history.append("\n")
        } else {
            binding.history.text = ""
        }
        // Add the representation of the new entry to the text view.
        val formatter = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())
        val text = String.format("%s [%s]", text, formatter.format(date))
        binding.history.append(text)
        isHistoryEmpty = false
    }
}