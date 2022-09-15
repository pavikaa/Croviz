package com.markopavicic.croviz.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.markopavicic.croviz.R
import com.markopavicic.croviz.databinding.FragmentHomeBinding
import com.markopavicic.croviz.ui.activity.EndlessQuizActivity
import com.markopavicic.croviz.ui.activity.StatsActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun launchStatsActivity() {
        val intent = Intent(requireContext(), StatsActivity::class.java)
        startActivity(intent)
    }

    private fun launchEndlessQuiz() {
        val intent = Intent(requireContext(), EndlessQuizActivity::class.java)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Scan QR code", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
        binding.cardEndlessFragmentHome.setOnClickListener{
            launchEndlessQuiz()
        }
        binding.cardStatsFragmentHome.setOnClickListener{
            launchStatsActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}