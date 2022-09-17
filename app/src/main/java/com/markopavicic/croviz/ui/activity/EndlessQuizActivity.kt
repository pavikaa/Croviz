package com.markopavicic.croviz.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.markopavicic.croviz.adapter.AnswerAdapter
import com.markopavicic.croviz.databinding.ActivityEndlessQuizBinding
import com.markopavicic.croviz.model.repository.QuizRepository
import com.markopavicic.croviz.viewmodel.EndlessViewModel

class EndlessQuizActivity : AppCompatActivity() {
    private val viewModel: EndlessViewModel by viewModels {
        EndlessViewModel.EndlessViewModelFactory(QuizRepository())
    }
    private var _binding: ActivityEndlessQuizBinding? = null

    private val binding get() = _binding!!

    private lateinit var answersRecyclerView: RecyclerView
    private lateinit var adapter: AnswerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEndlessQuizBinding.inflate(layoutInflater)
        viewModel.getAllQuizzes()
        viewModel.allQuizzes.observe(this) {
            setupViews()
        }
        setContentView(binding.root)
    }

    private fun setupViews() {
        val randomQuestion = viewModel.getRandomQuestion()

        answersRecyclerView = binding.rvAnswers
        binding.tvQuestion.text = randomQuestion.question
        val answers = randomQuestion.answers

        adapter = AnswerAdapter(this, answers)

        answersRecyclerView.adapter = adapter
        binding.btnAnswer.setOnClickListener {
            viewModel.finishQuestion(adapter.getResults())
            nextQuestion()
        }
    }

    private fun nextQuestion() {
        setupViews()
    }
}
