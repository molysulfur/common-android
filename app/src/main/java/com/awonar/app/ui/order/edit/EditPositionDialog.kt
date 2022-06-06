package com.awonar.app.ui.order.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.app.R
import com.awonar.app.databinding.AwonarDialogOrderEditBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.ui.order.OrderViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.DateUtils
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.extension.toast
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import timber.log.Timber

class EditPositionDialog :
    InteractorDialog<EditPositionMapper, EditPositionListener, DialogViewModel>() {

    private lateinit var binding: AwonarDialogOrderEditBinding

    private var position: Position? = null
    private var pl: Float = 0f
    private var price: Float = 0f
    private val viewModel: EditPositionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = AwonarDialogOrderEditBinding.inflate(inflater)
        binding.awonarOrderEditTextNumberpickerTp.setPrefix("$")
        binding.awonarOrderEditTextNumberpickerSl.setPrefix("$")
        binding.awonarOrderEditTextNumberpickerTp.setDescriptionColor(R.color.awonar_color_primary)
        binding.awonarOrderEditTextNumberpickerSl.setDescriptionColor(R.color.awonar_color_orange)
        launchAndRepeatWithViewLifecycle {
            viewModel.stopLossState.collect {
                binding.awonarOrderEditTextNumberpickerSl.setNumber(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.takeProfitState.collect {
                binding.awonarOrderEditTextNumberpickerTp.setNumber(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            QuoteSteamingManager.quotesState.collect { quotes ->
                val quote = quotes[position?.instrument?.id]
                quote?.let {
                    price = ConverterQuoteUtil.getCurrentPrice(
                        quote = it,
                        leverage = position?.leverage ?: 0,
                        isBuy = position?.isBuy == true
                    )
                    pl = PortfolioUtil.getProfitOrLoss(
                        current = price,
                        openRate = position?.openRate ?: 0f,
                        unit = position?.units ?: 0f,
                        conversionRate = 1f,
                        isBuy = position?.isBuy == true
                    )
                    val priceChange = ConverterQuoteUtil.change(
                        close = quote.close,
                        previous = quote.previous
                    )
                    val pricePercentChange = ConverterQuoteUtil.percentChange(
                        oldPrice = quote.previous,
                        newPrice = quote.close,
                    )
                    binding.pl = "$%.2f".format(pl)
                    binding.plPercent = "%.2f%s".format(
                        PortfolioUtil.profitLossPercent(pl, position?.amount ?: 0f),
                        "%"
                    )
                    binding.awonarOrderEditTextPl.setTextColor(
                        ColorChangingUtil.getTextColorChange(
                            requireContext(),
                            pl
                        )
                    )
                    binding.awonarOrderEditTextPlPercent.setTextColor(
                        ColorChangingUtil.getTextColorChange(
                            requireContext(),
                            pl
                        )
                    )
                    binding.price = "%s".format(price)
                    binding.priceChange = "%.2f (%.2f)".format(priceChange, pricePercentChange)
                    binding.status = it.status
                    binding.description = position?.instrument?.categories?.get(0) ?: ""
                    binding.awonarOrderEditTextChange.setTextColor(
                        ColorChangingUtil.getTextColorChange(
                            requireContext(),
                            priceChange
                        )
                    )
                }
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run {
            position = getParcelable(EXTRA_POSITION)
        }
        setInfo()
        setupState()
        setupListener()
    }

    private fun setupState() {
        position?.let {
            viewModel.setPositionType(it.isBuy)
            viewModel.setUnits(it.units)
            viewModel.setInstrumentId(it.instrument?.id ?: 0)
            viewModel.setStopLoss(it.stopLossRate ?: 0f)
            viewModel.setTakeProfit(it.takeProfitRate ?: 0f)
        }
    }

    private fun setupListener() {
        binding.awonarOrderEditToolbar.setOnClickListener {
            dismiss()
        }
        binding.awonarOrderEditTextNumberpickerTp.doAfterFocusChange = { number, isLeft ->
            val type = if (isLeft) {
                "amount"
            } else {
                "rate"
            }
        }
        binding.awonarOrderEditTextNumberpickerSl.doAfterFocusChange = { number, isLeft ->
        }
    }

    private fun setInfo() {
        ImageUtil.loadImage(binding.awonarOrderEditImageAvatar, position?.instrument?.logo)
        binding.awonarOrderEditTextTitle.text = "%s %s".format(
            if (position?.isBuy == true) "BUY" else "SELL",
            position?.instrument?.symbol
        )
        binding.amount = "$%.2f".format(position?.amount)
        binding.unit = "%.3f Unit".format(position?.units)
        binding.rate = "%s".format(position?.openRate)
        binding.date = "%s".format(DateUtils.getDate(position?.openDateTime))
        binding.fee = "Fee: $%.2f".format(position?.totalFees)
        binding.leverage = "X%s".format(position?.leverage)
        binding.id = position?.id
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        )
    }

    class Builder {
        private var key: String? = null
        private var data: Bundle? = null
        private var position: Position? = null

        fun setPosition(position: Position): Builder = this.apply {
            this.position = position
        }

        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setData(data: Bundle?): Builder = this.apply {
            this.data = data
        }

        fun build(): EditPositionDialog = newInstance(position, key, data)
    }

    companion object {

        private const val EXTRA_POSITION = "com.awonar.app.ui.order.edit.extra.position"

        private fun newInstance(
            position: Position?,
            key: String?,
            data: Bundle?,
        ) =
            EditPositionDialog().apply {
                arguments = createBundle(key, data).apply {
                    putParcelable(EXTRA_POSITION, position)
                }
            }
    }

    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<EditPositionMapper, EditPositionListener> =
        viewModel.orderEdit

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java
}