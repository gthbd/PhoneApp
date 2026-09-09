package com.example.phoneapp.model

enum class CallType {
    INCOMING, OUTGOING, MISSED
}

data class CallLogEntry(
    val id: Long,
    val displayName: String,   // tên đã lưu HOẶC số điện thoại nếu chưa lưu
    val callType: CallType,
    val date: String,          // ví dụ "Sep 4"
    // ví dụ "Vietnam" / "Hanoi City"
    val missedCount: Int = 1   // số lần gọi nhỡ liên tiếp — hiển thị "(3)" nếu > 1
)