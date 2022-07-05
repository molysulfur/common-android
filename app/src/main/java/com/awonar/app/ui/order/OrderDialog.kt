package com.awonar.app.ui.order

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
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
        /**
         * TP SL Observer
         */
        launchAndRepeatWithViewLifecycle {
            orderViewModel.minMaxSl.collectIndexed { _, value ->
                setMinMaxStopLossText(value)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.minMaxTp.collectIndexed { _, value ->
                setMinMaxTakeProfit(value)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.stopLossState.collect { sl ->
                setStopLossText(sl)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderViewModel.takeProfit.collect { tp ->
                setTakeProfitText(tp)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.showAmountSl.collectIndexed { _, _ ->
                setStopLossText(orderViewModel.stopLossState.value)
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.showAmountTp.collectIndexed { _, _ ->
                setTakeProfitText(orderViewModel.takeProfit.value)
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
        launchAndRepeatWithViewLifecycle {
            orderViewModel.amountState.collect { amount ->
                when (orderActivityViewModel.showAmount.value) {
                    false -> {
                        binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(0)
                        binding.awoanrDialogOrderButtonAmountToggle.text = "Amount"
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                            "%.2f".format(amount.second)
                        )
                    }
                    true -> {
                        binding.awoanrDialogOrderButtonAmountToggle.text =
                            "${amount.second} Units"
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                            "$%.2f".format(amount.first)
                        )
                    }
                }
                validateExposure(amount = amount.first)
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

    private fun setTakeProfitText(tp: Pair<Float, Float>) {
        Timber.e("$tp")
        when (orderActivityViewModel.showAmountTp.value) {
            false -> {
                binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpInputNumber.setStartIconDrawable(
                    0
                )
                binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpInputNumber.editText?.setText(
                    "%.2f".format(tp.second)
                )
                binding.awonarDialogOrderVerticalTitleTakeprofit.setSubTitle(
                    "$%.2f".format(
                        abs(tp.first)
                    )
                )
            }
            true -> {
                binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpInputNumber.editText?.setText(
                    "$%.2f".format(abs(tp.first))
                )
                binding.awonarDialogOrderVerticalTitleTakeprofit.setSubTitle("%s".format(tp.second))
            }
        }
        with(binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpSliderMinMax) {
            try {
                if (abs(tp.first) in valueFrom..valueTo) {
                    setValues(abs(tp.first))
                }
            } catch (e: IllegalStateException) {

            }
        }
    }

    private fun setStopLossText(sl: Pair<Float, Float>) {
        when (orderActivityViewModel.showAmountSl.value) {
            false -> {
                binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslButtonToggle.text = "Rate"
                binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslInputNumber.setStartIconDrawable(
                    0
                )
                binding.awonarDialogOrderVerticalTitleStoploss.setSubTitle("$%.2f".format(sl.first))
                binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslInputNumber.editText?.setText(
                    "%s".format(sl.second)
                )
            }
            true -> {
                binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslButtonToggle.text = "Amount"
                binding.awonarDialogOrderVerticalTitleStoploss.setSubTitle(
                    "%s".format(
                        abs(sl.second)
                    )
                )
                binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslInputNumber.editText?.setText(
                    "$%.2f".format(abs(sl.first))
                )
            }
        }
        with(binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax) {
            if (abs(sl.first) in valueFrom..valueTo) {
                setValues(abs(sl.first))
            }
        }
    }

    private fun setMinMaxStopLossText(stopLoss: Pair<Float, Float>) {
        if (stopLoss.first > 0) {
            orderViewModel.setAmountSl(stopLoss.second)
            binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax.setValues(stopLoss.second)
            binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax.valueFrom =
                stopLoss.first
            binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax.valueTo =
                stopLoss.second
            binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslTextForm.text =
                "-$%.2f".format(stopLoss.first)
            binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslTextTo.text =
                "-$%.2f".format(stopLoss.second)
        }
    }

    private fun setMinMaxTakeProfit(takeProfit: Pair<Float, Float>) {
        if (takeProfit.second > 0) {
            orderViewModel.setAmountTp(takeProfit.second)
            binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpSliderMinMax.setValues(takeProfit.second)
            binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpSliderMinMax.valueFrom =
                abs(takeProfit.first)
            binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpSliderMinMax.valueTo =
                abs(takeProfit.second)
            binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpTextForm.text =
                "$%.2f".format(abs(takeProfit.first))
            binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpTextTo.text =
                "$%.2f".format(takeProfit.second)
        }
    }

    private fun observeActivityViewModel() {
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.showAmount.collectIndexed { _, isShowAmount ->
                when (isShowAmount) {
                    true -> {
                        binding.awoanrDialogOrderButtonAmountToggle.text =
                            "${orderViewModel.amountState.value.second} Unit"
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                            "$%.2f".format(orderViewModel.amountState.value.first)
                        )
                    }
                    else -> {
                        binding.awoanrDialogOrderInputLayoutAmount.setStartIconDrawable(0)
                        binding.awoanrDialogOrderButtonAmountToggle.text =
                            "$%.2f Amount".format(orderViewModel.amountState.value.first)
                        binding.awoanrDialogOrderInputLayoutAmount.editText?.setText(
                            "%s".format(orderViewModel.amountState.value.second)
                        )
                    }
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            orderActivityViewModel.isSetRate.collectIndexed { _, isAtMarket ->
                binding.awoanrDialogOrderInputLayoutRate.apply {
                    isEnabled = isAtMarket
                    hint = if (isAtMarket) "" else "At Market"
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
            binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpContainer.visibility = View.GONE
            binding.awonarDialogOrderRecyclerLeverages.visibility = View.VISIBLE
            binding.awonarDialogOrderVerticalTitleLeverage.background =
                ContextCompat.getDrawable(requireContext(), R.color.awonar_color_light_gray)
            binding.awonarDialogOrderVerticalTitleStoploss.background =
                ContextCompat.getDrawable(requireContext(), R.color.awonar_color_surface)
            binding.awonarDialogOrderVerticalTitleTakeprofit.background =
                ContextCompat.getDrawable(requireContext(), R.color.awonar_color_surface)
        }
        binding.awonarDialogOrderVerticalTitleStoploss.setOnClickListener {
            binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslContainer.visibility =
                View.VISIBLE
            binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpContainer.visibility = View.GONE
            binding.awonarDialogOrderRecyclerLeverages.visibility = View.GONE
            binding.awonarDialogOrderVerticalTitleStoploss.background =
                ContextCompat.getDrawable(requireContext(), R.color.awonar_color_light_gray)
            binding.awonarDialogOrderVerticalTitleLeverage.background =
                ContextCompat.getDrawable(requireContext(), R.color.awonar_color_surface)
            binding.awonarDialogOrderVerticalTitleTakeprofit.background =
                ContextCompat.getDrawable(requireContext(), R.color.awonar_color_surface)

        }
        binding.awonarDialogOrderVerticalTitleTakeprofit.setOnClickListener {
            binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslContainer.visibility = View.GONE
            binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpContainer.visibility =
                View.VISIBLE
            binding.awonarDialogOrderRecyclerLeverages.visibility = View.GONE
            binding.awonarDialogOrderVerticalTitleTakeprofit.background =
                ContextCompat.getDrawable(requireContext(), R.color.awonar_color_light_gray)
            binding.awonarDialogOrderVerticalTitleLeverage.background =
                ContextCompat.getDrawable(requireContext(), R.color.awonar_color_surface)
            binding.awonarDialogOrderVerticalTitleStoploss.background =
                ContextCompat.getDrawable(requireContext(), R.color.awonar_color_surface)

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
                            amount = text.replace("$", "").toFloat(),
                            price = orderViewModel.priceState.value
                        )
                    }
                    false -> {
                        orderViewModel.updateUnits(
                            instrumentId = orderViewModel.instrument.value?.id ?: 0,
                            units = text.replace("$", "").toFloat(),
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
                            orderViewModel.updateRate(newRate.replace("$", "").toFloat())
                        }
                    }
                }
            }
        }

        binding.awoanrDialogOrderButtonRateToggle.setOnClickListener {
            val updateEnable = !binding.awoanrDialogOrderInputLayoutRate.isEnabled
            binding.awoanrDialogOrderButtonRateToggle.apply {
                text = if (!updateEnable) "Rate" else "At Market"
            }
            orderActivityViewModel.setAtMarket(updateEnable)
        }
    }

    private fun setupTakeProfit() {
        binding.awonarDialogOrderVerticalTitleTakeprofit.setSubTitleTextColor(R.color.awonar_color_green)
        with(binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpSliderMinMax) {
            addOnChangeListener { _, value, fromUser ->
                if (fromUser) {
                    orderViewModel.setAmountTp(value)
                }
            }
        }
        with(binding.awonarDialogOrderIncludeTp) {
            awonarIncludeSetTpSwitchNoset.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    awonarIncludeSetTpInputNumber.editText?.apply {
                        setText("")
                        hint = "No Set"
                        isEnabled = false
                    }
                } else {
                    awonarIncludeSetTpInputNumber.editText?.apply {
                        setTakeProfitText(orderViewModel.takeProfit.value)
                        hint = ""
                        isEnabled = true
                    }
                }
            }
            awonarIncludeSetTpInputNumber.editText?.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    val text = (v as EditText).text
                    when (orderActivityViewModel.showAmountTp.value) {
                        true -> {
                            try {
                                val newAmount =
                                    text.toString().replace("$", "").toFloatOrNull() ?: 0f
                                orderViewModel.setAmountTp(newAmount)
                            } catch (e: IllegalStateException) {
                                e.printStackTrace()
                            }
                        }
                        false -> orderViewModel.setRateTp(
                            text.toString().replace("$", "").toFloatOrNull()
                                ?: 0f
                        )
                    }
                }
            }
            awonarIncludeSetTpSwitchNoset.text = "Set TP"
            awonarIncludeSetTpButtonToggle.setOnClickListener {
                orderActivityViewModel.toggleShowTp()
            }
        }
        with(binding.awonarDialogOrderIncludeTp.awonarIncludeSetTpSliderMinMax) {
            addOnChangeListener { slider, value, fromUser ->
                if (fromUser) {
                    orderViewModel.setAmountTp(value)
                }
            }
        }
    }

    private fun setupStopLoss() {
        binding.awonarDialogOrderVerticalTitleStoploss.setSubTitleTextColor(R.color.awonar_color_orange)
        with(binding.awonarDialogOrderIncludeSl.awonarIncludeSetTpslSliderMinMax) {
            addOnChangeListener { _, value, fromUser ->
                if (fromUser) {
                    orderViewModel.setAmountSl(-value)
                }
            }
        }
        with(binding.awonarDialogOrderIncludeSl) {
            awonarIncludeSetTpslSwitchNoset.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    awonarIncludeSetTpslInputNumber.editText?.apply {
                        setText("")
                        hint = "No Set"
                        isEnabled = false
                    }
                } else {
                    awonarIncludeSetTpslInputNumber.editText?.apply {
                        setStopLossText(orderViewModel.stopLossState.value)
                        hint = ""
                        isEnabled = true
                    }
                }
            }
            awonarIncludeSetTpslInputNumber.editText?.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    val text = (v as EditText).text
                    when (orderActivityViewModel.showAmountSl.value) {
                        true -> {
                            val newAmount = text.toString().replace("$", "").toFloatOrNull()
                                ?: 0f
                            orderViewModel.setAmountSl(-newAmount)
                        }
                        false -> orderViewModel.setRateSl(
                            text.toString().replace("$", "").toFloatOrNull()
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