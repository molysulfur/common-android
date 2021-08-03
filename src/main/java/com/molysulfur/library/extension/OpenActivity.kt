package com.molysulfur.library.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.molysulfur.library.common.R

inline fun <reified T> Activity.openActivity(cls: Class<T>) {
    startActivity(Intent(this, cls))
    overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
}

inline fun <reified T> Activity.openActivity(cls: Class<T>, bundle: Bundle?) {
    startActivity(Intent(this, cls).apply {
        bundle?.let {
            putExtras(bundle)
        }
    })
    overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
}

inline fun <reified T> Activity.openActivityForResult(cls: Class<T>, requestCode: Int, bundle: Bundle?) {
    startActivityForResult(Intent(this, cls).apply {
        bundle?.let {
            putExtras(bundle)
        }
    }, requestCode)
    overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
}

inline fun <reified T> Activity.openActivityAndClearThisActivity(cls: Class<T>) {
    startActivity(Intent(this, cls))
    overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
    finish()
}

inline fun <reified T> Activity.openActivityAndClearThisActivity(cls: Class<T>, bundle: Bundle?) {
    startActivity(Intent(this, cls).apply {
        bundle?.let {
            putExtras(bundle)
        }
    })
    overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
    finish()
}

inline fun <reified T> Activity.openActivityAndClearAllActivity(cls: Class<T>) {
    startActivity(Intent(this, cls).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    })
    overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
    finish()
}

inline fun <reified T> Activity.openActivityAndClearAllActivity(cls: Class<T>, bundle: Bundle?) {
    startActivity(Intent(this, cls).apply {
        bundle?.let {
            putExtras(bundle)
        }
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    })
    overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
    finish()
}

inline fun <reified T> Fragment.openActivity(cls: Class<T>) {
    startActivity(Intent(activity, cls))
}

inline fun <reified T> Fragment.openActivity(cls: Class<T>, bundle: Bundle?) {
    startActivity(Intent(activity, cls).apply {
        bundle?.let {
            putExtras(bundle)
        }
    })
    activity?.overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
}

inline fun <reified T> Fragment.openActivityForResult(cls: Class<T>, requestCode: Int, bundle: Bundle?) {
    startActivityForResult(Intent(activity, cls).apply {
        bundle?.let {
            putExtras(bundle)
        }
    }, requestCode)
    activity?.overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
}

inline fun <reified T> Fragment.openActivityAndClearThisActivity(cls: Class<T>) {
    startActivity(Intent(activity, cls))
    activity?.overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
    activity?.finish()
}

inline fun <reified T> Fragment.openActivityAndClearThisActivity(cls: Class<T>, bundle: Bundle?) {
    startActivity(Intent(activity, cls).apply {
        bundle?.let {
            putExtras(bundle)
        }
    })
    activity?.overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
    activity?.finish()
}

inline fun <reified T> Fragment.openActivityAndClearAllActivity(cls: Class<T>) {
    startActivity(Intent(activity, cls).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    })
    activity?.overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
    activity?.finish()
}

inline fun <reified T> Fragment.openActivityAndClearAllActivity(cls: Class<T>, bundle: Bundle?) {
    startActivity(Intent(activity, cls).apply {
        bundle?.let {
            putExtras(bundle)
        }
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    })
    activity?.overridePendingTransition(R.anim.common_anim_left_to_right_in, R.anim.common_anim_left_to_right_out)
    activity?.finish()
}