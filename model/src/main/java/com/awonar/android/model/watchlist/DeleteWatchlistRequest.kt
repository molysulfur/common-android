package com.awonar.android.model.watchlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class DeleteWatchlistRequest(
    val folderId: String,
    val itemId: String,
) : Parcelable