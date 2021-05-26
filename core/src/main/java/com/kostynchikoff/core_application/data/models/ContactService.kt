package com.kostynchikoff.core_application.data.models

import android.os.Parcelable
import com.kostynchikoff.multiAdapter.SearchMultitype
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactsPhoneBook(
    val name: String,
    val phoneNumber: String,
    val avatar: String
): SearchMultitype(phoneNumber, name), Parcelable