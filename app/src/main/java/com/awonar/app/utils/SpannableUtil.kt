package com.awonar.app.utils

import android.content.Context
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.awonar.app.R

object SpannableUtil {

    fun getDontHaveAccountSpannable(context: Context, callable: ClickableSpan): SpannableString {
        val fullText =
            context.getString(R.string.awonar_hint_no_account_signup)
        val linkText =
            context.getString(R.string.awonar_span_no_account_signup)
        val start = fullText.indexOf(linkText)
        val end = start + linkText.length
        val spannableString = SpannableString(fullText)
        spannableString.setSpan(callable, start, end, 0)
        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    context,
                    R.color.awonar_color_primary
                )
            ), start, end, 0
        )
        return spannableString
    }


}