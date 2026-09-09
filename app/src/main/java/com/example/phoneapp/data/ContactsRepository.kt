package com.example.phoneapp.data

import android.content.Context
import android.provider.ContactsContract
import com.example.phoneapp.model.ContactEntry
import java.text.Normalizer
import java.util.Locale

object ContactsRepository {

    /**
     * Đọc toàn bộ danh bạ từ ContactsContract, sắp xếp theo tên
     * và nhóm theo chữ cái đầu (đã bỏ dấu để nhóm đúng, VD: "bố" -> "B").
     */
    fun getContactsGroupedByLetter(context: Context): Map<String, List<ContactEntry>> {
        val contacts = queryContacts(context)
        return contacts
            .sortedBy { it.name.lowercase(Locale.getDefault()) }
            .groupBy { firstLetterOf(it.name) }
            .toSortedMap()
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