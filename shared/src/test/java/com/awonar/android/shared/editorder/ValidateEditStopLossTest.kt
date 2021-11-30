package com.awonar.android.shared.editorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.awonar.android.exception.AddAmountException
import com.awonar.android.exception.RefundException
import com.awonar.android.model.Auth
import com.awonar.android.model.order.ValidateRateStopLossRequest
import com.awonar.android.shared.MainCoroutineRule
import com.awonar.android.shared.domain.order.ValidateRateStopLossUseCase
import com.awonar.android.shared.repos.CurrenciesRepository
import com.awonar.android.shared.repos.MarketRepository
import com.awonar.android.shared.utils.DecimalFormatUtil
import com.molysulfur.library.result.Result
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.text.DecimalFormat


@RunWith(MockitoJUnitRunner::class)
class ValidateEditStopLossTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var marketRepo: MarketRepository

    @Mock
    private lateinit var currenciesRepository: CurrenciesRepository

    private val testDispatcher = coroutineRule.testDispatcher

    @Before
    fun setUp() {
    }

    @Test
    fun `shouldBeAdd_39_34`() = testDispatcher.runBlockingTest {
        val useCase = ValidateRateStopLossUseCase(testDispatcher)
        val request = ValidateRateStopLossRequest(
            rateSl = 112f,
            amountSl = -289.33f,
            currentPrice = 113.254f,
            openPrice = 113.312f,
            amount = 500f,
            exposure = 25000f,
            units = 24999.55874f,
            leverage = 50,
            isBuy = true,
            digit = 3,
            available = 1000000f,
            conversionRate = 113.36f,
            maxStopLoss = -250f
        )
        val addAmount = 39.34f
        val expect =
            Result.Error(
                AddAmountException(
                    "Amount should be add \$%.2f".format(kotlin.math.abs(addAmount)),
                    addAmount
                )
            )
        val result = useCase.invoke(request)
        if (result is Result.Error) {
            Assert.assertEquals(
                (expect.exception as AddAmountException).value,
                DecimalFormatUtil.convert((result.exception as AddAmountException).value)
            )
        }

    }

    @Test
    fun `shouldBeRefund_22_05`() = testDispatcher.runBlockingTest {
        val useCase = ValidateRateStopLossUseCase(testDispatcher)
        val request = ValidateRateStopLossRequest(
            rateSl = 112.1f,
            amountSl = -267.29f,
            currentPrice = 113.254f,
            openPrice = 113.312f,
            amount = 539.34f,
            exposure = 25000f,
            units = 24999.55874f,
            leverage = 50,
            isBuy = true,
            digit = 3,
            available = 1000000f,
            conversionRate = 113.36f,
            maxStopLoss = -250f
        )
        val refund = -22.06f
        val expect =
            Result.Error(
                RefundException(
                    "Order should be refund \$%.2f".format(kotlin.math.abs(refund)),
                    refund
                )
            )
        val result = useCase.invoke(request)
        if (result is Result.Error) {
            Assert.assertEquals(
                (expect.exception as RefundException).value,
                DecimalFormatUtil.convert((result.exception as RefundException).value)
            )
        }
    }

    @Test
    fun `shouldBeRefund_17_29`() = testDispatcher.runBlockingTest {
        val useCase = ValidateRateStopLossUseCase(testDispatcher)
        val request = ValidateRateStopLossRequest(
            rateSl = 112.19f,
            amountSl = -247.44f,
            currentPrice = 113.254f,
            openPrice = 113.312f,
            amount = 517.29f,
            exposure = 25000f,
            units = 24999.55874f,
            leverage = 50,
            isBuy = true,
            digit = 3,
            available = 1000000f,
            conversionRate = 113.36f,
            maxStopLoss = -250f
        )
        val refund = -17.29f
        val expect =
            Result.Error(
                RefundException(
                    "Order should be refund \$%.2f".format(kotlin.math.abs(refund)),
                    refund
                )
            )
        val result = useCase.invoke(request)
        if (result is Result.Error) {
            Assert.assertEquals(
                (expect.exception as RefundException).value,
                DecimalFormatUtil.convert((result.exception as RefundException).value)
            )
        }
    }
}