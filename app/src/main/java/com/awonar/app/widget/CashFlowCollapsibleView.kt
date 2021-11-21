package com.awonar.app.widget

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.awonar.app.R
import com.awonar.app.databinding.AwonarWidgetCashflowCollaspsibleBinding
import com.awonar.app.databinding.AwonarWidgetCollapsibleLeverageBinding
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.widget.BaseViewGroup

class CashFlowCollapsibleView : BaseViewGroup {

    private lateinit var binding: AwonarWidgetCashflowCollaspsibleBinding

    private var title: String? = null
    private var titleRes: Int = 0
    private var logo: String? = null
    private var logoRes: Int = 0
    private var subTitle: String? = null
    private var subTitleRes: Int = 0
    private var description: String? = null
    private var descriptionRes: Int = 0
    private var amount: Float = 0f
    private var fee: Float = 0f
    private var rate: Float = 0f
    private var netWithdraw: Float = 0f
    private var localAmount: Float = 0f
    private var status: String? = null
    private var progress: Int = 0
    private var expanded = false
    private val toggle: Transition = TransitionInflater.from(context)
        .inflateTransition(R.transition.awonar_transition_list_toggle)

    override fun setup() {
        binding.awoanrCashflowCollaspibleViewContainer.setOnClickListener {
            toggleExpanded()
        }
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        updateProgress()
    }

    private fun updateProgress() {
        binding.awoanrCashflowCollapsibleViewProgressStatus.progress = progress
    }

    fun setStatus(status: String) {
        this.status = status
        updateStatus()
    }

    private fun updateStatus() {
        when {
            !status.isNullOrBlank() -> binding.awoanrCashflowCollapsibleViewTextStatus.text = status
            else -> binding.awoanrCashflowCollapsibleViewTextStatus.text = ""
        }
    }

    fun setLogo(logo: String) {
        this.logo = logo
        logoRes = 0
        updateLogo()
    }

    fun setLogo(logoRes: Int) {
        this.logoRes = logoRes
        logo = null
        updateLogo()
    }

    private fun updateLogo() {
        when {
            logo != null -> ImageUtil.loadImage(
                binding.awoanrCashflowCollapsibleViewImageLogo,
                logo
            )
            logoRes > 0 -> binding.awoanrCashflowCollapsibleViewImageLogo.setImageResource(logoRes)
        }
    }

    fun setLocalAmount(localAmount: Float) {
        this.localAmount = localAmount
        updateLocalAmount()
    }

    private fun updateLocalAmount() {
        binding.awoanrCashflowCollapsibleViewTextLocalAmount.text = "฿%.2f".format(localAmount)
    }

    fun setRate(rate: Float) {
        this.rate = rate
        updateRate()
    }

    private fun updateRate() {
        binding.awoanrCashflowCollapsibleViewTextRate.text = "%s".format(rate)
    }

    fun setFee(fee: Float) {
        this.fee = fee
        updateFee()
    }

    private fun updateFee() {
        binding.awoanrCashflowCollapsibleViewTextFee.text = "$%.2f".format(fee)
    }

    fun setAmount(amount: Float) {
        this.amount = amount
        updateAmount()
    }

    fun setNetWithdraw(netWithdraw: Float) {
        this.netWithdraw = netWithdraw
        updateNetWithdraw()
    }

    private fun updateNetWithdraw() {
        binding.awoanrCashflowCollapsibleViewTextNetWithdraw.text =
            "$%.2f".format(netWithdraw)
    }

    private fun updateAmount() {
        binding.awoanrCashflowCollapsibleViewTextAmount.text = "$%.2f".format(amount)
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
            description != null -> binding.awoanrCashflowCollapsibleViewTextDesciprtion.text =
                description
            descriptionRes > 0 -> binding.awoanrCashflowCollapsibleViewTextDesciprtion.setText(
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
            title != null -> binding.awoanrCashflowCollapsibleViewTextTitle.text = title
            titleRes > 0 -> binding.awoanrCashflowCollapsibleViewTextTitle.setText(titleRes)
        }
    }

    fun setSubTitle(subTitle: String) {
        this.subTitle = subTitle
        subTitleRes = 0
        updateSubTitle()
    }

    fun setSubTitle(subTitleRes: Int) {
        this.subTitleRes = subTitleRes
        subTitle = null
        updateSubTitle()
    }

    private fun updateSubTitle() {
        when {
            subTitle != null -> binding.awoanrCashflowCollapsibleViewTextSubtitle.text =
                subTitle
            subTitleRes > 0 -> binding.awoanrCashflowCollapsibleViewTextSubtitle.setText(
                subTitleRes
            )
        }
    }

    override fun getLayoutResource(): View {
        binding = AwonarWidgetCashflowCollaspsibleBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }


    override fun setupStyleables(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
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
        binding.awoanrCashflowCollapsibleViewContainerExpend.visibility =
            if (expanded) View.VISIBLE else View.GONE
        binding.awoanrCashflowCollapsibleViewButtonExpend.rotationX = if (expanded) 180f else 0f
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