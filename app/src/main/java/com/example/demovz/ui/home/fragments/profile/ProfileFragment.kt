package com.example.demovz.ui.home.fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demovz.R
import com.example.demovz.databinding.FragmentProfileBinding
import com.example.demovz.ui.review_settings.ReviewSettingsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.appBar.txtTitle.text = getString(R.string.title_profile)

        binding.apply {
            name.setOnClickListener{
                startActivity(Intent(requireActivity(), ReviewSettingsActivity::class.java))
            }
        }

        return root
    }

}