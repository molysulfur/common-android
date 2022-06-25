package com.awonar.app.ui.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.akexorcist.library.dialoginteractor.DialogLauncher
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.app.R
import com.awonar.app.databinding.AwonarDialogOrderBinding
import com.awonar.app.dialog.DialogViewModel
import com.awonar.app.ui.order.leverageselector.adpater.LeverageSelectorAdapter
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.google.android.material.textfield.TextInputEditText
import com.molysulfur.library.extension.toast
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs

class OrderDialog : InteractorDialog<OrderMapper, OrderDialogListener, DialogViewModel>() {

    private val binding: AwonarDialogOrderBinding by lazy {
        AwonarDialogOrderBinding.inflate(layoutInflater)
    }

    private val orderViewModel: OrderViewModel by activityViewModels()
    private val orderActivityViewModel: OrderViewModelActivity by activityViewModels()
    private val portfolioViewModel: PortFolioViewModel by activityViewModels()
    private val leverageAdapter: LeverageSelectorAdapter = LeverageSelectorAdapter().apply {
        onChecked = {
            orderViewModel.updateLeverage(it)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        observeActivityViewModel()
        launchAndRepeatWithViewLifecycle {
            orderViewModel.minMaxSl.collectIndexed { index, value ->
                if (
                    binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax.valueFrom <= 0 &&
                    binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax.valueTo <= 0 &&
                    value.first > 0
                ) {
                    binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslTextForm.text =
                        "$%.2f".format(value.first)
                    binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax.valueFrom =
                        value.first
                    binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslTextTo.text =
                        "$%.2f".format(value.second)
                    binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax.valueTo =
                        value.second
                    binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax.setValues(
                        abs(value.first)
                    )
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.minMaxTp.collectIndexed { index, value ->
                if (
                    binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslSliderMinMax.valueFrom <= 0 &&
                    binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslSliderMinMax.valueTo <= 0 &&
                    value.second > 0
                ) {
                    binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslTextForm.text =
                        "$%.2f".format(abs(value.first))
                    binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslSliderMinMax.valueFrom =
                        abs(value.first)
                    binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslTextTo.text =
                        "$%.2f".format(value.second)
                    binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslSliderMinMax.valueTo =
                        abs(value.second)
                    binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslSliderMinMax.setValues(
                        abs(value.first)
                    )
                }
            }
        }
        /**
         * instrument observe
         */
        launchAndRepeatWithViewLifecycle {
            orderViewModel.instrument.collect { _ ->

            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.depositState.collectIndexed { _, isDeposit ->
                if (isDeposit) {
                    binding.awonarDialogOrderButtonOpenTrade.text = "Deposit"
                } else {
                    binding.awonarDialogOrderButtonOpenTrade.text = "Open Trade"
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.overnightFeeMessage.collectIndexed { _, value ->
                binding.awonarDialogOrderTextOrderOvernight.text = value
            }
        }
        /**
         * Current Price and Changed
         */
        launchAndRepeatWithViewLifecycle {
            orderViewModel.priceState.collect {
                binding.awonarDialogOrderTextPrice.text =
                    "%.${orderViewModel.instrument.value?.digit}f".format(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.changeState.collect {
                binding.awonarDialogOrderTextChange.text =
                    "%.${orderViewModel.instrument.value?.digit}f (%.2f%s)".format(
                        it.first,
                        it.second, "%"
                    )
            }
        }
        /**
         * streaming data
         */
        launchAndRepeatWithViewLifecycle {
            QuoteSteamingManager.quotesState.collect { quotes ->
                orderViewModel.updatePrice(quotes)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.bidState.collect { bid ->
                binding.awonarDialogOrderButtonTypeBuy.text = "BUY $bid"
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.askState.collect { ask ->
                binding.awonarDialogOrderButtonTypeSell.text = "Sell $ask"
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.tradingData.collect { tradingData ->
                binding.awonarDialogOrderButtonTypeBuy.isEnabled = tradingData?.allowBuy == true
                binding.awonarDialogOrderButtonTypeSell.isEnabled = tradingData?.allowSell == true
            }
        }
        launchAndRepeatWithViewLifecycle {
            portfolioViewModel.portfolioState.collect {
                if (it != null) {
                    orderViewModel.setPortfolio(it)
                }
            }
        }
        /**
         * Amount Obsever
         */
        launchAndRepeatWithViewLifecycle {
            launch {
                orderActivityViewModel.exposureError.collect { error ->
                    binding.awoanrDialogOrderInputLayoutAmount.error = ""
                    error?.let {
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText("${it.value}")
                        binding.awoanrDialogOrderInputLayoutAmount.error = (it.message ?: "")
                        orderViewModel.updateAmount(
                            instrumentId = orderViewModel.instrument.value?.id ?: 0,
                            amount = it.value,
                            price = orderViewModel.priceState.value
                        )
                    }
                }
            }
            launch {
                orderViewModel.amountState.collect { amount ->
                    when (orderActivityViewModel.showAmount.value) {
                        false -> {
                            binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(0)
                            binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                                "%.2f".format(amount.second)
                            )
                        }
                        true -> {
                            binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(R.drawable.awoanr_ic_dollar)
                            binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                                "%.2f".format(amount.first)
                            )
                        }
                    }
                    validateExposure(amount = amount.first)
                }
            }
        }
        /**
         * Rate Obsever
         */
        launchAndRepeatWithViewLifecycle(Lifecycle.State.RESUMED) {
            launch {
                orderActivityViewModel.rateErrorState.collect {
                    binding.awoanrDialogOrderInputLayoutRate.error = ""
                    it?.let {
                        binding.awoanrDialogOrderInputLayoutRate.editText?.setText("${it.rate}")
                        binding.awoanrDialogOrderInputLayoutRate.error = (it.message ?: "")
                    }
                }
            }
            launch {
                orderViewModel.rateState.collect { rate ->
                    binding.awoanrDialogOrderInputLayoutRate.editText?.setText("${rate ?: ""}")
                    rate?.let {
                        orderActivityViewModel.validateRate(
                            it,
                            orderViewModel.priceState.value,
                            orderViewModel.instrument.value?.digit ?: 0
                        )
                    }
                }
            }
        }
        /**
         * TP SL Observer
         */
        launchAndRepeatWithViewLifecycle {
            orderViewModel.takeProfit.collect { tp ->
                when (orderActivityViewModel.showAmountTp.value) {
                    false -> {
                        binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslInputNumber.setStartIconDrawable(
                            0
                        )
                        binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslInputNumber.editText?.setText(
                            "%.2f".format(tp.second)
                        )
                        binding.awonarDialogOrderVerticalTitleTakeprofit.setSubTitle(
                            "$%.2f".format(
                                abs(tp.first)
                            )
                        )
                    }
                    true -> {
                        binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslInputNumber.setStartIconDrawable(
                            R.drawable.awoanr_ic_dollar
                        )
                        binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslInputNumber.editText?.setText(
                            "%.2f".format(abs(tp.first))
                        )
                        binding.awonarDialogOrderVerticalTitleTakeprofit.setSubTitle("%s".format(tp.second))
                    }
                }
                val slider = binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslSliderMinMax
                if (slider.valueTo > 0 && tp.first >= slider.valueFrom && tp.first <= slider.valueTo) {
                    binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslSliderMinMax.setValues(
                        abs(tp.first)
                    )
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.stopLossState.collect { sl ->
                when (orderActivityViewModel.showAmountSl.value) {
                    false -> {
                        binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslInputNumber.setStartIconDrawable(
                            0
                        )
                        binding.awonarDialogOrderVerticalTitleStoploss.setSubTitle("$%.2f".format(sl.first))
                        binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslInputNumber.editText?.setText(
                            "%s".format(sl.second)
                        )
                    }
                    true -> {
                        binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslInputNumber.setStartIconDrawable(
                            R.drawable.awoanr_ic_dollar
                        )
                        binding.awonarDialogOrderVerticalTitleStoploss.setSubTitle(
                            "%s".format(
                                abs(sl.first)
                            )
                        )
                        binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslInputNumber.editText?.setText(
                            "%.2f".format(abs(sl.first))
                        )
                    }
                }
                val slider = binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax
                Timber.e("$sl")
                if (slider.valueTo > 0 && sl.first >= slider.valueFrom && sl.first <= slider.valueTo) {
                    binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax.setValues(
                        abs(sl.first)
                    )
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            launch {
                orderViewModel.leverageState.collect { leverage ->
                    binding.awonarDialogOrderVerticalTitleLeverage.setSubTitle("X$leverage")
                    val tradingData = orderViewModel.tradingData.value
                    if (binding.awonarDialogOrderRecyclerLeverages.adapter == null && tradingData != null) {
                        with(binding.awonarDialogOrderRecyclerLeverages) {
                            this.adapter = leverageAdapter
                            layoutManager =
                                GridLayoutManager(requireContext(), tradingData.leverages.size)
                        }
                    }
                    if (tradingData != null) {
                        val newList = tradingData.leverages.mapIndexed { index, s ->
                            LeverageSelectorAdapter.StepViewData(
                                label = "X$s",
                                value = s.toInt(),
                                start = index == 0,
                                end = index == tradingData.leverages.size - 1,
                                isCheck = s.toInt() == leverage
                            )
                        }.toMutableList()
                        leverageAdapter.itemList = newList
                    }
//                    binding.awonarDialogOrderCollapseLeverage.setDescription("X$it")
//                    getOvernight()
                }
            }
            launch {
                orderViewModel.openOrderState.collect { message ->
                    dismiss()
                    toast(message)
                }
            }

        }
        binding.viewModel = orderViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun observeActivityViewModel() {
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.showAmount.collectIndexed { _, isShowAmount ->
                when (isShowAmount) {
                    true -> {
                        binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(R.drawable.awoanr_ic_dollar)
                        binding.awoanrDialogOrderButtonAmountToggle.text = "Unit"
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                            "%.2f".format(orderViewModel.amountState.value.first)
                        )
                    }
                    else -> {
                        binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(0)
                        binding.awoanrDialogOrderButtonAmountToggle.text = "Amount"
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                            "%.2f".format(orderViewModel.amountState.value.second)
                        )

                    }
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.showAmountTp.collectIndexed { _, isShowAmount ->
                when (isShowAmount) {
                    true -> {
                        binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslInputNumber.setStartIconDrawable(
                            R.drawable.awoanr_ic_dollar
                        )
                        binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslInputNumber.editText?.setText(
                            "%.2f".format(abs(orderViewModel.takeProfit.value.first))
                        )
                        binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslButtonToggle.text =
                            "Rate"
                    }
                    else -> {
                        binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslInputNumber.setStartIconDrawable(
                            0
                        )
                        binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslInputNumber.editText?.setText(
                            "%s".format(orderViewModel.takeProfit.value.second)
                        )
                        binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslButtonToggle.text =
                            "Amount"

                    }
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.showAmount.collectIndexed { _, isShowAmount ->
                when (isShowAmount) {
                    true -> {
                        binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(R.drawable.awoanr_ic_dollar)
                        binding.awoanrDialogOrderButtonAmountToggle.text = "Unit"
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                            "%.2f".format(orderViewModel.amountState.value.first)
                        )
                    }
                    else -> {
                        binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(0)
                        binding.awoanrDialogOrderButtonAmountToggle.text = "Amount"
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                            "%s".format(orderViewModel.amountState.value.second)
                        )

                    }
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.showAmountSl.collectIndexed { _, isShowAmount ->
                when (isShowAmount) {
                    true -> {
                        with(binding.awonarDialogOrderIncludeSl) {
                            awonarIncludeSetTpslInputNumber.setStartIconDrawable(R.drawable.awoanr_ic_dollar)
                            awonarIncludeSetTpslInputNumber.editText?.setText(
                                "%.2f".format(-orderViewModel.stopLossState.value.first)
                            )
                            awonarIncludeSetTpslButtonToggle.text = "Rate"
                        }
                    }
                    else -> {
                        with(binding.awonarDialogOrderIncludeSl) {
                            awonarIncludeSetTpslInputNumber.setStartIconDrawable(0)
                            awonarIncludeSetTpslInputNumber.editText?.setText(
                                "%s".format(orderViewModel.stopLossState.value.second)
                            )
                            awonarIncludeSetTpslButtonToggle.text = "Amount"
                        }
                    }
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.showAmountTp.collectIndexed { _, isShowAmount ->
                when (isShowAmount) {
                    true -> {
                        with(binding.awonarDialogOrderIncludeTp) {
                            awonarIncludeSetTpslInputNumber.setStartIconDrawable(R.drawable.awoanr_ic_dollar)
                            awonarIncludeSetTpslInputNumber.editText?.setText(
                                "%.2f".format(abs(orderViewModel.takeProfit.value.first))
                            )
                            awonarIncludeSetTpslButtonToggle.text = "Rate"
                        }
                    }
                    else -> {
                        with(binding.awonarDialogOrderIncludeTp) {
                            awonarIncludeSetTpslInputNumber.setStartIconDrawable(0)
                            awonarIncludeSetTpslInputNumber.editText?.setText(
                                "%s".format(orderViewModel.takeProfit.value.second)
                            )
                            awonarIncludeSetTpslButtonToggle.text = "Amount"
                        }
                    }
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.isSetRate.collectIndexed { _, isAtMarket ->
                binding.awoanrDialogOrderInputLayoutRate.apply {
                    isEnabled = isAtMarket
                    hint = if (isAtMarket) "Open Rate" else "At Market"
                }
                when (isAtMarket) {
                    false -> orderViewModel.updateRate(null)
                    true -> orderViewModel.updateRate(orderViewModel.priceState.value)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val instrument = arguments?.getParcelable<Instrument?>(EXTRA_INSTRUMENT)
        val isBuy = arguments?.getBoolean(EXTRA_TYPE, true)
        orderViewModel.setInstrument(instrument)
        with(binding.awonarDialogOrderToggleOrderType) {
            orderViewModel.setIsBuy(isBuy)
            when (isBuy) {
                true -> check(R.id.awonar_dialog_order_button_type_buy)
                false -> check(R.id.awonar_dialog_order_button_type_sell)
                else -> {}
            }
        }
        binding.awonarDialogOrderToggleOrderType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.awonar_dialog_order_button_type_sell -> orderViewModel.setIsBuy(false)
                    R.id.awonar_dialog_order_button_type_buy -> orderViewModel.setIsBuy(true)
                }
            }

        }
        binding.awonarDialogOrderImageIconClose.setOnClickListener {
            dismiss()
        }
        binding.awonarDialogOrderButtonOpenTrade.setOnClickListener {
            orderViewModel.submit()
        }
        setupRate()
        setupAmount()
        setupTabs()
        setupStopLoss()
        setupTakeProfit()
    }

    private fun setupTabs() {
        binding.awonarDialogOrderVerticalTitleLeverage.setOnClickListener {
            binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslContainer.visibility = View.GONE
            binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslContainer.visibility = View.GONE
            binding.awonarDialogOrderRecyclerLeverages.visibility = View.VISIBLE
        }
        binding.awonarDialogOrderVerticalTitleStoploss.setOnClickListener {
            binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslContainer.visibility =
                View.VISIBLE
            binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslContainer.visibility = View.GONE
            binding.awonarDialogOrderRecyclerLeverages.visibility = View.GONE
        }
        binding.awonarDialogOrderVerticalTitleTakeprofit.setOnClickListener {
            binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslContainer.visibility = View.GONE
            binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslContainer.visibility =
                View.VISIBLE
            binding.awonarDialogOrderRecyclerLeverages.visibility = View.GONE
        }
    }

    private fun setupAmount() {
        with(binding.awoanrDialogOrderButtonAmountToggle) {
            setOnClickListener {
                orderActivityViewModel.toggleShowAmount()
            }
        }
        binding.awoanrDialogOrderInputLayoutAmount.editText?.setOnFocusChangeListener { v, hasFocus ->
            val text = (v as TextInputEditText).text.toString()
            if (!hasFocus) {
                when (orderActivityViewModel.showAmount.value) {
                    true -> {
                        orderViewModel.updateAmount(
                            instrumentId = orderViewModel.instrument.value?.id ?: 0,
                            amount = text.toFloat(),
                            price = orderViewModel.priceState.value
                        )
                    }
                    false -> {
                        orderViewModel.updateUnits(
                            instrumentId = orderViewModel.instrument.value?.id ?: 0,
                            units = text.toFloat(),
                            price = orderViewModel.priceState.value
                        )
                    }
                }
            }
        }
    }

    private fun setupRate() {
        binding.awoanrDialogOrderInputLayoutRate.editText?.let { editText ->
            with(editText) {
                setOnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        val newRate = (v as EditText).text.toString()
                        if (newRate.isNotBlank()) {
                            orderViewModel.updateRate(newRate.toFloat())
                        }
                    }
                }
            }
        }

        binding.awoanrDialogOrderButtonRateToggle.setOnClickListener {
            val updateEnable = !binding.awoanrDialogOrderInputLayoutRate.isEnabled
            orderActivityViewModel.setAtMarket(updateEnable)
        }
    }

    private fun setupTakeProfit() {
        with(binding.awonarDialogOrderIncludeTp) {
            awonarIncludeSetTpslInputNumber.editText?.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    val text = (v as EditText).text
                    when (orderActivityViewModel.showAmountTp.value) {
                        true -> {
                            try {
                                val newAmount = text.toString().toFloatOrNull() ?: 0f
                                orderViewModel.setAmountSl(newAmount)
                                val slider =
                                    binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslSliderMinMax
                                if (newAmount >= slider.valueFrom && newAmount <= slider.valueTo) {
                                    binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslSliderMinMax.setValues(
                                        newAmount
                                    )
                                }
                            } catch (e: IllegalStateException) {
                                e.printStackTrace()
                            }
                        }
                        false -> orderViewModel.setRateTp(
                            text.toString().toFloatOrNull()
                                ?: 0f
                        )
                    }
                }
            }
            awonarIncludeSetTpslSwitchNoset.text = "Set TP"
            awonarIncludeSetTpslButtonToggle.setOnClickListener {
                orderActivityViewModel.toggleShowTp()
            }
        }
        with(binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpslSliderMinMax) {
            addOnChangeListener { slider, value, fromUser ->
                if (fromUser) {
                    orderViewModel.setAmountTp(value)
                }
            }
        }
    }

    private fun setupStopLoss() {
        with(binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax) {
            addOnChangeListener { slider, value, fromUser ->
                if (fromUser) {
                    orderViewModel.setAmountSl(-value)
                }
            }
        }
        with(binding.awonarDialogOrderIncludeSl) {
            awonarIncludeSetTpslInputNumber.editText?.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    val text = (v as EditText).text
                    when (orderActivityViewModel.showAmountSl.value) {
                        true -> {
                            val newAmount = text.toString().toFloatOrNull()
                                ?: 0f
                            orderViewModel.setAmountSl(-newAmount)
                            val slider =
                                binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax
                            if (newAmount >= slider.valueFrom && newAmount <= slider.valueTo) {
                                binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax.setValues(
                                    newAmount
                                )
                            }
                        }
                        false -> orderViewModel.setRateSl(
                            text.toString().toFloatOrNull()
                                ?: 0f
                        )
                    }
                }
            }
            awonarIncludeSetTpslSwitchNoset.text = "Set SL"
            awonarIncludeSetTpslButtonToggle.setOnClickListener {
                orderActivityViewModel.toggleShowSl()
            }
        }
    }

    private fun validateExposure(amount: Float) {
        orderViewModel.instrument.value?.let { instrument ->
            if (orderViewModel.leverageState.value > 0) {
                orderActivityViewModel.validateExposure(
                    id = instrument.id,
                    amount = amount,
                    leverage = orderViewModel.leverageState.value
                )
            }
        }
    }

    private fun getOvernight() {
//        instrument?.let { instrument ->
//            orderActivityViewModel.getOvernightFeeDaliy(
//                instrumentId = instrument.id,
//                orderType = isBuy
//            )
//            orderActivityViewModel.getOvernightFeeWeek(
//                instrumentId = instrument.id,
//                orderType = isBuy
//            )
//        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        )
    }

    class Builder {
        private var instrument: Instrument? = null
        private var isBuy: Boolean = true
        private var key: String? = null
        private var data: Bundle? = null

        fun setSymbol(instrument: Instrument?): Builder = this.apply {
            this.instrument = instrument
        }

        fun setType(isBuy: Boolean): Builder = this.apply {
            this.isBuy = isBuy
        }

        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setData(data: Bundle?): Builder = this.apply {
            this.data = data
        }

        fun build(): OrderDialog = newInstance(instrument, isBuy, key, data)
    }

    override fun bindLauncher(viewModel: DialogViewModel): DialogLauncher<OrderMapper, OrderDialogListener> =
        viewModel.order

    override fun bindViewModel(): Class<DialogViewModel> = DialogViewModel::class.java

    companion object {
        private const val EXTRA_INSTRUMENT = "com.awonar.app.ui.dialog.order.extra.instrument"
        private const val EXTRA_TYPE = "com.awonar.app.ui.dialog.order.extra.order_type"

        private fun newInstance(
            instrument: Instrument?,
            isBuy: Boolean,
            key: String?,
            data: Bundle?,
        ) =
            OrderDialog().apply {
                arguments = createBundle(key, data).apply {
                    putParcelable(EXTRA_INSTRUMENT, instrument)
                    putBoolean(EXTRA_TYPE, isBuy)
                }
            }
    }

    override fun onDestroy() {
        viewModelStore.clear()
        super.onDestroy()
    }
}