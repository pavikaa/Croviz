package com.markopavicic.croviz.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markopavicic.croviz.R
import com.markopavicic.croviz.adapter.AnswerCreationAdapter
import com.markopavicic.croviz.databinding.FragmentQuizCreationQuestionsBinding
import com.markopavicic.croviz.model.data.Answer
import com.markopavicic.croviz.model.repository.QuizRepository
import com.markopavicic.croviz.ui.activity.QuizDetailsActivity
import com.markopavicic.croviz.utils.Constants
import com.markopavicic.croviz.viewmodel.QuizCreationViewModel
import com.markopavicic.croviz.viewmodel.QuizCreationViewModelFactory

class QuizCreationQuestionsFragment : Fragment() {
    private val viewModel: QuizCreationViewModel by activityViewModels {
        QuizCreationViewModelFactory(QuizRepository())
    }
    private var _binding: FragmentQuizCreationQuestionsBinding? = null

    private val binding get() = _binding!!

    private val args: QuizCreationQuestionsFragmentArgs by navArgs()

    private lateinit var answersRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizCreationQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddAnswerCreation.setOnClickListener {
            if (checkAnswerInput()) {
                val answer = binding.tfQuizAnswerCreation.editText?.text.toString()
                val isCorrect = binding.chkCorrectAnswerCreation.isChecked
                clearAnswerInput()
                viewModel.addAnswer(Answer(isCorrect, answer))
                answersRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
        binding.btnAddQuestionCreation.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.add_question))
                .setMessage(getString(R.string.add_question_details))
                .setNegativeButton(getString(R.string.finish_quiz_creation)) { dialog, which ->
                    if (addQuestion())
                        finishQuizCreation()
                }
                .setPositiveButton(getString(R.string.add_another_question)) { dialog, which ->
                    if (addQuestion())
                        addAnotherQuestion()
                }
                .show()
        }
        answersRecyclerView = binding.rvAnswersCreation
        viewModel.clearAnswers()
        setupRecycler(viewModel.answers)
    }

    private fun addAnotherQuestion() {
        clearQuestionInput()
        clearAnswerInput()
        clearAnswers()
    }

    private fun clearAnswerInput() {
        binding.tfQuizAnswerCreation.editText?.setText("")
    }

    private fun addQuestion(): Boolean {
        if (checkQuestionInput()) {
            val question = binding.tfQuizQuestionCreation.editText?.text.toString()
            val answers = viewModel.answers.value?.toList()

            if (answers != null) {
                viewModel.addQuestion(question, answers)
                return true
            }
        }
        return false
    }

    private fun checkQuestionInput(): Boolean {
        val tfQuestion = binding.tfQuizQuestionCreation
        if (TextUtils.isEmpty(tfQuestion.editText?.text)) {
            tfQuestion.error = getString(R.string.error_required)
            return false
        } else {
            tfQuestion.error = ""
        }
        if (!viewModel.checkAnswers()) {
            binding.tfQuizAnswerCreation.error =
                getString(R.string.error_answers)
            return false
        }
        return true
    }

    private fun checkAnswerInput(): Boolean {
        val tfAnswer = binding.tfQuizAnswerCreation
        if (TextUtils.isEmpty(tfAnswer.editText?.text)) {
            tfAnswer.error = getString(R.string.error_required)
            return false
        } else {
            tfAnswer.error = ""
        }
        return true
    }

    private fun setupRecycler(answers: LiveData<MutableList<Answer>>) {
        answersRecyclerView.adapter = context?.let { AnswerCreationAdapter(it, answers.value!!) }
    }

    private fun clearAnswers() {
        viewModel.clearAnswers()
        answersRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun clearQuestionInput() {
        binding.tfQuizQuestionCreation.editText?.setText("")
    }

    private fun finishQuizCreation() {
        if (viewModel.questions.value?.isEmpty()!!) {
            binding.tfQuizQuestionCreation.error =
                getString(R.string.error_questions)
        } else {
            val quizName = args.quizName
            val quizCategory = args.quizCategory
            val hideCreator = args.hideCreator
            val quizId = viewModel.submitQuiz(quizName, quizCategory, hideCreator)

            val intent = Intent(context, QuizDetailsActivity::class.java)
            intent.putExtra(Constants.QUIZ_ID_KEY, quizId)
            startActivity(intent)
            activity?.finish()
        }
    }
}