package com.markopavicic.croviz.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.markopavicic.croviz.databinding.ActivityQuizBinding
import com.markopavicic.croviz.model.repository.QuizRepository
import com.markopavicic.croviz.utils.Constants
import com.markopavicic.croviz.viewmodel.QuizViewModel
import com.markopavicic.croviz.viewmodel.QuizViewModelFactory

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(QuizRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = getKeyFromIntent()
        if (key.isNotEmpty()) {
            viewModel.getQuizById(key)
        }
    }

    private fun getKeyFromIntent(): String {
        return intent.getStringExtra(Constants.QUIZ_ID_KEY)!!
    }
}