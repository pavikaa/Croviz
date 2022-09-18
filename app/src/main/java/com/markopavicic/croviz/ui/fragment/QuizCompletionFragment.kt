package com.markopavicic.croviz.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.markopavicic.croviz.R
import com.markopavicic.croviz.databinding.FragmentQuizCompletionBinding
import com.markopavicic.croviz.model.repository.QuizRepository
import com.markopavicic.croviz.viewmodel.QuizViewModel
import com.markopavicic.croviz.viewmodel.QuizViewModelFactory

class QuizCompletionFragment : Fragment() {

    private var _binding: FragmentQuizCompletionBinding? = null

    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(QuizRepository())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizCompletionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvEarnedPoints.text =
            getString(R.string.quiz_results) + viewModel.points + getString(R.string.points)
        binding.btnHome.setOnClickListener {
            activity?.finish()
        }
    }
}