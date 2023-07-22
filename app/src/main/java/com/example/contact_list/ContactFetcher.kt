package com.example.contact_list

import android.content.ContentResolver
import android.provider.ContactsContract
import javax.inject.Inject


class ContactFetcher @Inject constructor(private val contentResolver: ContentResolver) {

    fun fetchContacts(): List<Contact> {
        val contactsList = ArrayList<Contact>()
        val phoneNumbersSet = HashSet<String>() // HashSet to keep track of unique phone numbers

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.RawContacts.ACCOUNT_TYPE
        )

        val selection =
            "${ContactsContract.RawContacts.ACCOUNT_TYPE} <> 'com.telegram.messenger' AND ${ContactsContract.CommonDataKinds.Phone.NUMBER} IS NOT NULL AND ${ContactsContract.CommonDataKinds.Phone.NUMBER} <> ''"

        val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                var phoneNumber = it.getString(phoneIndex)

                // Remove all non-numeric characters from phone number
                phoneNumber = phoneNumber.replace(Regex("[^\\d]"), "")

                // Check if the phone number is not a duplicate
                if (!phoneNumbersSet.contains(phoneNumber) && phoneNumber.length >= 9) {
                    phoneNumbersSet.add(phoneNumber)
                    contactsList.add(Contact(name, phoneNumber))
                }
            }
        }

        cursor?.close()

        return contactsList
    }
}
