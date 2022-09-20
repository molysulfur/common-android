package com.molysulfur.library.widget

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.customview.view.AbsSavedState

abstract class BaseViewGroup : FrameLayout {
    constructor(context: Context) : super(context) {
        setup(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setup(attrs, defStyleAttr)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        setup(attrs, defStyleAttr, defStyleRes)
    }

    private fun setup(attrs: AttributeSet?, defStyleAttr: Int = 0, defStyleRes: Int = 0) {
        attrs?.let {
            setupStyleables(attrs, defStyleAttr, defStyleRes)
        }
        inflateLayout()
        setup()
    }

    private fun inflateLayout() {
        addView(getLayoutResource())
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is ChildSavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        restoreInstanceState(state)
        onRestoreInstanceChildState(state)
    }

    override fun onSaveInstanceState(): Parcelable? {
        return saveInstanceState(super.onSaveInstanceState())
    }

    protected fun onSaveInstanceChildState(ss: ChildSavedState): Parcelable {
        ss.childrenStates = SparseArray()
        for (i in 0 until childCount) {
            val id = getChildAt(i).id
            if (id != 0) {
                ss.childrenStates?.let { savedChildrenState ->
                    val childrenState = SparseArray<Parcelable>()
                    getChildAt(i).saveHierarchyState(childrenState)
                    savedChildrenState.put(id, childrenState)
                }
            }

        }
        return ss
    }

    private fun onRestoreInstanceChildState(ss: ChildSavedState) {
        for (i in 0 until childCount) {
            val id = getChildAt(i).id
            if (id != 0) {
                ss.childrenStates?.get(id)?.let { childrenState ->
                    @Suppress("UNCHECKED_CAST")
                    getChildAt(i).restoreHierarchyState(childrenState as SparseArray<Parcelable>)
                }
                if (ss.childrenStates?.get(id) != null) {
                }
            }
        }
    }

    abstract class ChildSavedState : AbsSavedState {
        var childrenStates: SparseArray<Any>? = null

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel) {
            childrenStates = parcel.readSparseArray(classLoader)
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            childrenStates?.let { childrenStates ->
                out.writeSparseArray(childrenStates)
            }
        }
    }

    abstract fun getLayoutResource(): View

    abstract fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)

    abstract fun setup()

    abstract fun saveInstanceState(state: Parcelable?): Parcelable?

    abstract fun restoreInstanceState(state: Parcelable)
}
