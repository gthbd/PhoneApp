package com.example.phoneapp.data

import android.content.Context
import android.provider.CallLog
import com.example.phoneapp.model.CallLogEntry
import com.example.phoneapp.model.CallType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CallLogRepository {

    /**
     * Đọc toàn bộ lịch sử cuộc gọi từ CallLog provider của hệ thống,
     * sau đó gộp các cuộc gọi liên tiếp cùng 1 số/tên thành 1 dòng
     * (giống hành vi hiển thị "(3)" của app Điện thoại thật).
     */
    fun getCallLogEntries(context: Context): List<CallLogEntry> {
        val rawRows = queryRawCallLog(context)
        return groupConsecutiveCalls(rawRows)
    }

    private data class RawCallLogRow(
        val displayName: String,
        val callType: CallType,
        val timestampMillis: Long
    )

    private fun queryRawCallLog(context: Context): List<RawCallLogRow> {
        val projection = arrayOf(
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE
        )

        val rows = mutableListOf<RawCallLogRow>()

        context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            null,
            null,
            "${CallLog.Calls.DATE} DESC"
        )?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)
            val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)

            while (cursor.moveToNext()) {
                val cachedName = cursor.getString(nameIndex)
                val number = cursor.getString(numberIndex) ?: ""
                val type = cursor.getInt(typeIndex)
                val timestamp = cursor.getLong(dateIndex)

                rows.add(
                    RawCallLogRow(
                        displayName = cachedName?.takeIf { it.isNotBlank() } ?: number,
                        callType = mapCallType(type),
                        timestampMillis = timestamp
                    )
                )
            }
        }

        return rows
    }

    private fun mapCallType(type: Int): CallType {
        return when (type) {
            CallLog.Calls.OUTGOING_TYPE -> CallType.OUTGOING
            CallLog.Calls.MISSED_TYPE, CallLog.Calls.REJECTED_TYPE -> CallType.MISSED
            else -> CallType.INCOMING
        }
    }

    private fun groupConsecutiveCalls(rows: List<RawCallLogRow>): List<CallLogEntry> {
        val grouped = mutableListOf<CallLogEntry>()
        var index = 0
        var idCounter = 0L

        while (index < rows.size) {
            val current = rows[index]
            var groupSize = 1

            while (
                index + groupSize < rows.size &&
                rows[index + groupSize].displayName == current.displayName
            ) {
                groupSize++
            }

            grouped.add(
                CallLogEntry(
                    id = idCounter++,
                    displayName = current.displayName,
                    callType = current.callType,
                    date = formatDate(current.timestampMillis),
                    missedCount = groupSize
                )
            )

            index += groupSize
        }

        return grouped
    }

    private fun formatDate(timestampMillis: Long): String {
        val formatter = SimpleDateFormat("MMM d", Locale.getDefault())
        return formatter.format(Date(timestampMillis))
    }
}