package com.example.phoneapp.data

import android.content.ContentProviderOperation
import android.content.Context
import android.provider.ContactsContract
import com.example.phoneapp.model.ContactEntry
import java.text.Normalizer
import java.util.Locale

object ContactsRepository {

    fun getContactsGroupedByLetter(
        context: Context,
        ascending: Boolean = true
    ): List<Pair<String, List<ContactEntry>>> {
        val contacts = queryContacts(context)

        val sortedContacts = if (ascending) {
            contacts.sortedBy { it.name.lowercase(Locale.getDefault()) }
        } else {
            contacts.sortedByDescending { it.name.lowercase(Locale.getDefault()) }
        }

        val grouped = sortedContacts.groupBy { firstLetterOf(it.name) }

        val sortedLetters = if (ascending) {
            grouped.keys.sorted()
        } else {
            grouped.keys.sortedDescending()
        }

        return sortedLetters.map { letter -> letter to (grouped[letter] ?: emptyList()) }
    }

    fun getPhoneNumbersForContact(context: Context, contactId: Long): List<String> {
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val selection = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
        val selectionArgs = arrayOf(contactId.toString())

        val numbers = mutableListOf<String>()

        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (cursor.moveToNext()) {
                val number = cursor.getString(numberIndex)
                if (!number.isNullOrBlank()) {
                    numbers.add(number)
                }
            }
        }

        return numbers.distinct()
    }

    /**
     * Thêm 1 liên hệ mới vào danh bạ hệ thống.
     * Việc ghi vào ContactsContract cần gộp nhiều thao tác (tạo RawContact,
     * gắn tên, gắn số điện thoại, gắn email) thành 1 giao dịch duy nhất (batch)
     * để đảm bảo toàn vẹn dữ liệu — nếu 1 bước lỗi, toàn bộ batch bị huỷ, không tạo ra liên hệ nửa vời.
     */
    fun insertContact(
        context: Context,
        name: String,
        phoneNumber: String,
        email: String
    ): Boolean {
        if (name.isBlank()) return false

        val operations = ArrayList<ContentProviderOperation>()

        // Bước 1: tạo "khung" liên hệ rỗng (RawContact) — mọi thông tin khác sẽ gắn vào khung này.
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        // Bước 2: gắn tên vào khung vừa tạo ở bước 1 (tham chiếu bằng index 0 = thao tác đầu tiên).
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build()
        )

        // Bước 3 (tuỳ chọn): gắn số điện thoại nếu người dùng có nhập.
        if (phoneNumber.isNotBlank()) {
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                    .withValue(
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                    )
                    .build()
            )
        }

        // Bước 4 (tuỳ chọn): gắn email nếu người dùng có nhập.
        if (email.isNotBlank()) {
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                    .withValue(
                        ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK
                    )
                    .build()
            )
        }

        return try {
            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun queryContacts(context: Context): List<ContactEntry> {
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        )

        val contacts = mutableListOf<ContactEntry>()

        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val name = cursor.getString(nameIndex) ?: continue
                contacts.add(ContactEntry(id = id, name = name))
            }
        }

        return contacts
    }

    private fun firstLetterOf(name: String): String {
        val firstChar = name.trim().firstOrNull() ?: return "#"
        val normalized = Normalizer.normalize(firstChar.toString(), Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}"), "")
        val letter = normalized.firstOrNull()?.uppercaseChar() ?: return "#"
        return if (letter.isLetter()) letter.toString() else "#"
    }
}