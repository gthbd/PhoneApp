package com.example.phoneapp.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat

object PhoneCallHelper {

    /**
     * Gọi trực tiếp tới số điện thoại nếu đã có quyền CALL_PHONE.
     * Nếu vì lý do nào đó chưa có quyền (VD: người dùng thu hồi quyền thủ công
     * trong Cài đặt sau khi app đã chạy), rơi về mở app Điện thoại với số đã điền sẵn
     * (ACTION_DIAL không cần quyền) thay vì làm crash app.
     */
    fun placeCall(context: Context, phoneNumber: String) {
        if (phoneNumber.isBlank()) return

        val hasCallPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED

        val action = if (hasCallPermission) Intent.ACTION_CALL else Intent.ACTION_DIAL
        val intent = Intent(action, Uri.parse("tel:$phoneNumber"))
        context.startActivity(intent)
    }
}