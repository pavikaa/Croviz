package com.markopavicic.croviz.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.markopavicic.croviz.R
import com.markopavicic.croviz.databinding.FragmentHomeBinding
import com.markopavicic.croviz.ui.activity.EndlessQuizActivity
import com.markopavicic.croviz.ui.activity.QrActivity
import com.markopavicic.croviz.ui.activity.QuizCreationActivity
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
            val modalBottomSheet = ModalBottomSheet()
            modalBottomSheet.show(childFragmentManager, ModalBottomSheet.TAG)
        }
        binding.cardEndlessFragmentHome.setOnClickListener {
            launchEndlessQuiz()
        }
        binding.cardStatsFragmentHome.setOnClickListener {
            launchStatsActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ModalBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.modal_bottom_sheet_content, container, false)

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialButton>(R.id.btn_new_quiz).setOnClickListener {
            launchNewQuiz()
        }
        view.findViewById<MaterialButton>(R.id.btn_scan_qr).setOnClickListener {
            launchScanQr()
        }
    }

    private fun launchScanQr() {
        val intent = Intent(context, QrActivity::class.java)
        startActivity(intent)
    }

    private fun launchNewQuiz() {
        val intent = Intent(context, QuizCreationActivity::class.java)
        startActivity(intent)
    }
}