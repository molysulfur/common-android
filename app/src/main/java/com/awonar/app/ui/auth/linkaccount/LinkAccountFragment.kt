package com.awonar.app.ui.auth.linkaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.awonar.app.databinding.AwonarFragmentLinkAccountBinding

class LinkAccountFragment : Fragment() {
    private val binding: AwonarFragmentLinkAccountBinding by lazy {
        AwonarFragmentLinkAccountBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.awonarLinkAccountToolbarLinkAccount.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = arguments?.getString("email")
        binding.email = email
    }
}