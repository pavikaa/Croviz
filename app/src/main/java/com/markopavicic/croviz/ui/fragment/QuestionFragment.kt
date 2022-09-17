package com.markopavicic.croviz.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.markopavicic.croviz.R
import com.markopavicic.croviz.adapter.AnswerAdapter
import com.markopavicic.croviz.databinding.FragmentQuestionBinding
import com.markopavicic.croviz.model.data.Answer
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.model.repository.QuizRepository
import com.markopavicic.croviz.viewmodel.QuizViewModel
import com.markopavicic.croviz.viewmodel.QuizViewModelFactory

class QuestionFragment : Fragment() {

    private var questionNo = 0

    private val viewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(QuizRepository())
    }
    private var _binding: FragmentQuestionBinding? = null

    private val binding get() = _binding!!
    private lateinit var answersRecyclerView: RecyclerView

    private lateinit var adapter: AnswerAdapter
    private lateinit var answers: List<Answer>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.quiz.observe(viewLifecycleOwner) { quiz ->
            setupViews(quiz)
        }
    }

    private fun setupViews(quiz: Quiz) {
        binding.tvQuestionNumber.text =
            "Question " + (questionNo + 1).toString() + " out of " + quiz.questions.size.toString()
        answersRecyclerView = binding.rvAnswers
        binding.tvQuestion.text = quiz.questions[0].question
        answers = quiz.questions[0].answers

        binding.btnAnswer.setOnClickListener {
            if (questionNo + 1 < quiz.questions.size) {
                nextQuestion(quiz)
            } else {
                completeQuiz()
            }
        }
        setupRecycler(answers)
    }

    private fun nextQuestion(quiz: Quiz) {
        viewModel.postAnswers(adapter.getResults())
        questionNo += 1
        binding.tvQuestionNumber.text =
            "Question " + (questionNo + 1).toString() + " out of " + quiz.questions.size.toString()
        binding.tvQuestion.text = quiz.questions[questionNo].question
        answers = quiz.questions[questionNo].answers
        setupRecycler(answers)
    }

    private fun completeQuiz() {
        viewModel.postAnswers(adapter.getResults())
        viewModel.completeQuiz()
        findNavController().navigate(R.id.action_questionFragment_to_quizCompletionFragment)
    }

    private fun setupRecycler(answers: List<Answer>) {
        context?.let { adapter = AnswerAdapter(it, answers) }
        answersRecyclerView.adapter = adapter
    }
}