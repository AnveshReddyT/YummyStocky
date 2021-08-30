package app.yummy.stocky.screens.details

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import app.yummy.stocky.R
import app.yummy.stocky.base.BaseFragment
import app.yummy.stocky.databinding.FragmentDetailBinding
import com.google.android.material.chip.Chip

class DetailFragment : BaseFragment<DetailsViewModel, FragmentDetailBinding>(R.layout.fragment_detail) {

    private val args: DetailFragmentArgs by navArgs()

    override fun getViewModel(): DetailsViewModel =
        ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mViewModel
        mViewModel.fetchStockDetails(args.id)
        mViewModel.stock.observe(viewLifecycleOwner) { stock ->
            stock.companyType.forEach {
                val chip = Chip(requireContext())
                chip.text = it
                mBinding.chipGroup.addView(chip)
            }
        }
        mBinding.websiteLink.apply {
            paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }
    }
}
