package com.awonar.app.ui.publishfeed

import com.linkedin.android.spyglass.mentions.Mentionable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MentionSuggestion(
    val suggestion: String,
) : Mentionable {

    override fun getSuggestibleId(): Int = suggestion.hashCode()

    override fun getSuggestiblePrimaryText(): String = suggestion

    override fun getTextForDisplayMode(mode: Mentionable.MentionDisplayMode): String = when (mode) {
        Mentionable.MentionDisplayMode.FULL -> "@$suggestion"
        else -> ""
    }

    override fun getDeleteStyle(): Mentionable.MentionDeleteStyle =
        Mentionable.MentionDeleteStyle.PARTIAL_NAME_DELETE
}