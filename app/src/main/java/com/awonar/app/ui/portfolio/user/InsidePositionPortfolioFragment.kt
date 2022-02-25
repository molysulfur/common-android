package com.awonar.app.ui.portfolio.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.awonar.android.model.portfolio.PublicPosition
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentInsidePortfolioPublicBinding
import com.awonar.app.ui.portfolio.PortFolioViewModel
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.inside.PositionInsideViewModel
import com.awonar.app.ui.portfolio.position.PositionViewModel
import com.awonar.app.ui.user.UserViewModel
import com.awonar.app.utils.ColorChangingUtil
import com.awonar.app.utils.ImageUtil
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class InsidePositionPortfolioFragment : Fragment() {

    private val binding: AwonarFragmentInsidePortfolioPublicBinding by lazy {
        AwonarFragmentInsidePortfolioPublicBinding.inflate(layoutInflater)
    }

    private val positionViewModel: PositionViewModel by activityViewModels()
    private val insideViewModel: PositionInsideViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private val viewModel: PortFolioViewModel by activityViewModels()

    private val args: InsidePositionPortfolioFragmentArgs by navArgs()

    private var currentIndex: Int = 0

    private var position: PublicPosition? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        currentIndex = args.currentIndex
        val currentPosition = viewModel.getPositionIndex(currentIndex)
        currentPosition.let {
            Timber.e("${it?.instrument?.symbol} $currentIndex")
            val user = userViewModel.userState.value
            positionViewModel.getInsidePublic(user?.username,
                it?.instrument?.symbol)
        }
        launchAndRepeatWithViewLifecycle {
            launchAndRepeatWithViewLifecycle {
                positionViewModel.publicPosition.collect {
                    position = it
                    insideViewModel.convertPublicPosition(it?.positions ?: emptyList())
                    setupInfo()
                }
            }
        }
        binding.column1 = "Amount"
        binding.column2 = "Open"
        binding.column3 = "Current"
        binding.column4 = "P/L(%)"
        binding.viewModel = insideViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.awonarInsidePortfolioPublicToolbar) {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.awonar_menu_next -> {
                        val currentPosition = viewModel.getPositionIndex(++currentIndex)
                        currentPosition.let {
                            val user = userViewModel.userState.value
                            positionViewModel.getInsidePublic(user?.username,
                                it?.instrument?.symbol)
                        }
                    }
                    R.id.awonar_menu_prev -> {
                        val currentPosition = viewModel.getPositionIndex(--currentIndex)
                        currentPosition.let {
                            val user = userViewModel.userState.value
                            positionViewModel.getInsidePublic(user?.username,
                                it?.instrument?.symbol)
                        }
                    }
                }
                true
            }
        }
    }

    private fun setupInfo() {
        position?.let {
            ImageUtil.loadImage(binding.awonarInsidePortfolioPublicImageLogo,
                it.positions[0].instrument?.logo)
            binding.awonarInsidePortfolioPublicTextTitle.text = it.symbol
            binding.awonarInsidePortfolioPublicTextSubtitle.text = it.positions[0].instrument?.name
            binding.awonarInsidePortfolioPublicVerticalTitleInvested.setTitle("%.2f%s".format(it.invested,
                "%"))
            binding.awonarInsidePortfolioPublicVerticalTitleAvgopen.setTitle("%.2f".format(it.avgOpen))
            binding.awonarInsidePortfolioPublicVerticalTitlePl.setTitle("%.2f%s".format(it.netProfit,
                "%"))
            binding.awonarInsidePortfolioPublicVerticalTitlePl.setTitleTextColor(ColorChangingUtil.getTextColorChange(
                requireContext(),
                it.netProfit))
        }
    }
}