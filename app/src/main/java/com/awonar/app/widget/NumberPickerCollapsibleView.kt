package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetCollapsibleNumberpickerBinding
import com.molysulfur.library.widget.BaseViewGroup

class NumberPickerCollapsibleView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetCollapsibleNumberpickerBinding
    private var title: String? = null
    private var titleRes: Int = 0
    private var description: String? = null
    private var descriptionRes: Int = 0
    private var descriptionColor: Int = 0
    private var digit: Int = 0
    private var prefix: String = ""
    private var expanded = false
    private var isNoSet = true
    private var visibleNoSet = false
    private var isLeft = true
    private val toggle: Transition = TransitionInflater.from(context)
        .inflateTransition(R.transition.awonar_transition_list_toggle)

    private lateinit var number: Pair<Float, Float>

    var doAfterFocusChange: ((Pair<Float, Float>, Boolean) -> Unit)? = null
    var doAfterTextChange: ((Float) -> Unit)? = null
    var doAfterToggle: ((Boolean) -> Unit)? = null

    override fun setup() {
        number = Pair(0f, 0f)
        binding.awonarNumberpickerCollapsibleToggleOrderAmountType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                isLeft = checkedId == R.id.awonar_numberpicker_collapsible_button_amount_amount
                doAfterToggle?.invoke(isLeft)
                updateNumber()
            }
        }
        binding.awonarNumberpickerCollapsibleLayoutCollapse.setOnClickListener {
            toggleExpanded()
        }
        binding.awonarNumberpickerCollapsibleButtonNoSet.setOnClickListener {
            isNoSet = !isNoSet
            setDescription("No set")
            updateNoSet()
        }
        binding.awonarNumberpickerCollapsibleInputNumber.doAfterFocusChange = { number, hasFocus ->
            if (!hasFocus) {
                doAfterFocusChange?.invoke(this.number, isLeft)
            }
        }
        binding.awonarNumberpickerCollapsibleInputNumber.doAfterTextChange = {
            number = when (isLeft) {
                true -> {
                    Pair(it, this.number.second)
                }
                else -> {
                    Pair(this.number.first, it)
                }
            }
            doAfterTextChange?.invoke(number.second)
        }
        binding.awonarNumberpickerCollapsibleInputNumber.setPlaceholder("No set")
        updateNoSet()
        updateTitle()
        updateDescription()
        updateNumber()
    }

    fun visibleNoSet(visible: Boolean) {
        visibleNoSet = visible
        updateHideNoSet()
    }

    private fun updateHideNoSet() {
        binding.awonarNumberpickerCollapsibleButtonNoSet.visibility =
            if (visibleNoSet) View.VISIBLE else View.GONE
    }

    fun setPrefix(prefix: String) {
        this.prefix = prefix
        updateNumber()
    }


    fun setHelp(help: String) {
        binding.awonarNumberpickerCollapsibleInputNumber.setHelp(help)
    }

    fun setNumber(number: Pair<Float, Float>) {
        this.number = number
        updateNumber()
    }


    fun setDigit(digit: Int) {
        this.digit = digit
        updateDigit()
    }

    private fun updateDigit() {
        binding.awonarNumberpickerCollapsibleInputNumber.setDigit(digit)
    }

    private fun updateNumber() {
        when (isLeft) {
            true -> {
                binding.awonarNumberpickerCollapsibleInputNumber.setNumber(number.first)
                binding.awonarNumberpickerCollapsibleInputNumber.setPrefix(prefix)
            }
            else -> {
                binding.awonarNumberpickerCollapsibleInputNumber.setNumber(number.second)
                binding.awonarNumberpickerCollapsibleInputNumber.setPrefix("")
            }
        }
    }

    private fun updateNoSet() {
        binding.awonarNumberpickerCollapsibleInputNumber.setPlaceHolderEnable(isNoSet)
    }

    fun setDescriptionColor(color: Int) {
        this.descriptionColor = color
        updateDescriptionColor()
    }

    private fun updateDescriptionColor() {
        if (descriptionColor > 0)
            binding.awonarNumberpickerCollapsibleTextDescription.setTextColor(
                ContextCompat.getColor(
                    context,
                    descriptionColor
                )
            )
    }

    fun setDescription(descriptionRes: Int) {
        this.descriptionRes = descriptionRes
        description = null
        updateDescription()
    }

    fun setDescription(description: String) {
        this.description = description
        descriptionRes = 0
        updateDescription()
    }

    private fun updateDescription() {
        when {
            description != null -> binding.awonarNumberpickerCollapsibleTextDescription.text =
                description
            descriptionRes > 0 -> binding.awonarNumberpickerCollapsibleTextDescription.setText(
                descriptionRes
            )
        }
    }

    fun setTitle(titleRes: Int) {
        this.titleRes = titleRes
        title = null
        updateTitle()
    }


    fun setTitle(title: String) {
        this.title = title
        titleRes = 0
        updateTitle()
    }

    private fun updateTitle() {
        when {
            title != null -> {
                binding.awonarNumberpickerCollapsibleTextTitle.text = title
                binding.awonarNumberpickerCollapsibleTextTitleAmount.text = title
            }
            titleRes > 0 -> {
                binding.awonarNumberpickerCollapsibleTextTitle.setText(titleRes)
                binding.awonarNumberpickerCollapsibleTextTitleAmount.setText(titleRes)
            }
        }
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetCollapsibleNumberpickerBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }


    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.NumberPickerCollapsibleView)
        title =
            typedArray.getString(R.styleable.NumberPickerCollapsibleView_numberPickerCollapsibleView_setTitle)
        description =
            typedArray.getString(R.styleable.NumberPickerCollapsibleView_numberPickerCollapsibleView_setDescription)
//        number =
//            typedArray.getFloat(
//                R.styleable.NumberPickerCollapsibleView_numberPickerCollapsibleView_setNumber,
//                0f
//            )
        digit =
            typedArray.getInt(
                R.styleable.NumberPickerCollapsibleView_numberPickerCollapsibleView_setDigit,
                0
            )
        typedArray.recycle()
    }

    override fun saveInstanceState(state: Parcelable?): Parcelable? {
        return state
    }

    override fun restoreInstanceState(state: Parcelable) {
    }

    private fun toggleExpanded() {
        expanded = !expanded
        toggle.duration = if (expanded) 300L else 200L
        TransitionManager.beginDelayedTransition(binding.root.parent as ViewGroup, toggle)
        binding.awonarNumberpickerCollapsibleGroupView.visibility =
            if (expanded) View.VISIBLE else View.GONE
        binding.awonarNumberpickerCollapsibleImageIconExpand.rotationX = if (expanded) 180f else 0f
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


}