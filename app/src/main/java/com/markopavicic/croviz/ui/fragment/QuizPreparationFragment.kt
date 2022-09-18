package com.markopavicic.croviz.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.markopavicic.croviz.R
import com.markopavicic.croviz.databinding.FragmentQuizPreparationBinding
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.model.repository.QuizRepository
import com.markopavicic.croviz.viewmodel.QuizViewModel
import com.markopavicic.croviz.viewmodel.QuizViewModelFactory

class QuizPreparationFragment : Fragment() {

    private var _binding: FragmentQuizPreparationBinding? = null

    private val binding get() = _binding!!
    private val viewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(QuizRepository())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizPreparationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.quiz.observe(viewLifecycleOwner) { quiz ->
            setupViews(quiz)
        }
    }

    private fun setupViews(quiz: Quiz) {
        binding.quizName.text = quiz.quizName
        binding.quizCreator.text = quiz.creator
        when (quiz.quizCategory) {
            "general_knowledge" -> {
                binding.quizImage.setImageDrawable(context?.getDrawable(R.drawable.general))
                binding.quizCategory.text = getText(R.string.general_knowledge)
            }
            "music" -> {
                binding.quizImage.setImageDrawable(context?.getDrawable(R.drawable.music))
                binding.quizCategory.text = getText(R.string.music)
            }
            "tv" -> {
                binding.quizImage.setImageDrawable(context?.getDrawable(R.drawable.tv))
                binding.quizCategory.text = getText(R.string.tv)
            }
            "science" -> {
                binding.quizImage.setImageDrawable(context?.getDrawable(R.drawable.science))
                binding.quizCategory.text = getText(R.string.science)
            }
            "history" -> {
                binding.quizImage.setImageDrawable(context?.getDrawable(R.drawable.history))
                binding.quizCategory.text = getText(R.string.history)
            }
            "geography" -> {
                binding.quizImage.setImageDrawable(context?.getDrawable(R.drawable.geography))
                binding.quizCategory.text = getText(R.string.geography)
            }
            else -> {
                binding.quizImage.setImageDrawable(context?.getDrawable(R.drawable.sports))
                binding.quizCategory.text = getText(R.string.sports)
            }
        }
        binding.btnStartQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_quizPreparationFragment_to_questionFragment)
        }
    }
}