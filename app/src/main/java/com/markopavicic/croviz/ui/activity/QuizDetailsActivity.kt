package com.markopavicic.croviz.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.markopavicic.croviz.R
import com.markopavicic.croviz.databinding.ActivityQuizDetailsBinding
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.model.repository.QuizRepository
import com.markopavicic.croviz.utils.Constants
import com.markopavicic.croviz.viewmodel.QuizViewModel
import com.markopavicic.croviz.viewmodel.QuizViewModelFactory

class QuizDetailsActivity : AppCompatActivity() {

    private lateinit var key: String

    private val viewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(QuizRepository())
    }
    private lateinit var binding: ActivityQuizDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizDetailsBinding.inflate(layoutInflater)
        key = getKeyFromIntent()
        if (key.isNotEmpty()) {
            viewModel.getQuizById(key)
            Log.d("quiz", viewModel.quiz.value.toString())
            viewModel.quiz.observe(this) { quiz ->
                setupViews(quiz)
                Log.d("quiz", quiz.toString())
            }
        }

        setContentView(binding.root)

    }

    private fun setupViews(quiz: Quiz) {
        binding.quizName.text = quiz.quizName
        binding.quizCreator.text = quiz.creator
        when (quiz.quizCategory) {
            "general_knowledge" -> {
                binding.quizImage.setImageDrawable(getDrawable(R.drawable.general))
                binding.quizCategory.text = getText(R.string.general_knowledge)
            }
            "music" -> {
                binding.quizImage.setImageDrawable(getDrawable(R.drawable.music))
                binding.quizCategory.text = getText(R.string.music)
            }
            "tv" -> {
                binding.quizImage.setImageDrawable(getDrawable(R.drawable.tv))
                binding.quizCategory.text = getText(R.string.tv)
            }
            "science" -> {
                binding.quizImage.setImageDrawable(getDrawable(R.drawable.science))
                binding.quizCategory.text = getText(R.string.science)
            }
            "history" -> {
                binding.quizImage.setImageDrawable(getDrawable(R.drawable.history))
                binding.quizCategory.text = getText(R.string.history)
            }
            "geography" -> {
                binding.quizImage.setImageDrawable(getDrawable(R.drawable.geography))
                binding.quizCategory.text = getText(R.string.geography)
            }
            else -> {
                binding.quizImage.setImageDrawable(getDrawable(R.drawable.sports))
                binding.quizCategory.text = getText(R.string.sports)
            }
        }
        showQrFromKey()
    }

    private fun showQrFromKey() {
        val writer = MultiFormatWriter()
        try {
            val matrix = writer.encode(key, BarcodeFormat.QR_CODE, 250, 250)
            val encoder = BarcodeEncoder()
            val bitmap = encoder.createBitmap(matrix)
            binding.quizQr.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

    }

    private fun getKeyFromIntent(): String {
        return intent.getStringExtra(Constants.QUIZ_ID_KEY)!!
    }
}