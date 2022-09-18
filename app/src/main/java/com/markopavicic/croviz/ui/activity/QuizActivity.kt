package com.markopavicic.croviz.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.markopavicic.croviz.R
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
    private var quizExists: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = getKeyFromIntent()
        if (key.isNotEmpty()) {
            QuizRepository().checkIfQuizExists(key, quizExists)
            quizExists.observe(this) {
                if (it) {
                    viewModel.getQuizById(key)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.wrong_qr),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }

    private fun getKeyFromIntent(): String {
        return intent.getStringExtra(Constants.QUIZ_ID_KEY)!!
    }
}