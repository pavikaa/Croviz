package com.markopavicic.croviz.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.markopavicic.croviz.databinding.ActivityQuizDetailsBinding
import com.markopavicic.croviz.utils.Constants

class QuizDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizDetailsBinding.inflate(layoutInflater)
        val key = getKeyFromIntent()
        binding.key.text = key
        setContentView(binding.root)

    }

    private fun getKeyFromIntent(): String {
        return intent.getStringExtra(Constants.QUIZ_ID_KEY)!!
    }
}