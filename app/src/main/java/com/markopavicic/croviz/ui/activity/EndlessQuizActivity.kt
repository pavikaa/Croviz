package com.markopavicic.croviz.ui.activity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.markopavicic.croviz.R
import com.markopavicic.croviz.adapter.AnswerAdapter
import com.markopavicic.croviz.databinding.ActivityEndlessQuizBinding
import com.markopavicic.croviz.model.data.Answer
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
    private lateinit var answers: List<Answer>

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEndlessQuizBinding.inflate(layoutInflater)
        viewModel.getAllQuizzes()
        viewModel.allQuizzes.observe(this) {
            setupViews()
        }
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupViews() {
        val randomQuestion = viewModel.getRandomQuestion()

        answersRecyclerView = binding.rvAnswers
        binding.tvQuestion.text = randomQuestion.question
        answers = randomQuestion.answers

        adapter = AnswerAdapter(this, answers)

        answersRecyclerView.adapter = adapter
        binding.btnAnswer.setOnClickListener {
            viewModel.finishQuestion(adapter.getResults())
            nextQuestion()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun nextQuestion() {
        showCorrectAnswers()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                setupViews()
            },
            2000 // value in milliseconds
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showCorrectAnswers() {
        for (position in 0 until answersRecyclerView.childCount) {
            val holder = answersRecyclerView.findViewHolderForAdapterPosition(position)
            if (answers[position].correct) {
                holder?.itemView?.findViewById<MaterialCardView>(R.id.answer_card)?.strokeColor =
                    getColor(R.color.green)
            } else
                holder?.itemView?.findViewById<MaterialCardView>(R.id.answer_card)?.strokeColor =
                    getColor(R.color.red)
        }
    }
}
