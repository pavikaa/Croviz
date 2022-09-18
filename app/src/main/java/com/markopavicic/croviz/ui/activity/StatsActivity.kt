package com.markopavicic.croviz.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.markopavicic.croviz.R
import com.markopavicic.croviz.model.repository.QuizRepository
import com.markopavicic.croviz.viewmodel.StatsViewModel
import com.markopavicic.croviz.viewmodel.StatsViewModelFactory


class StatsActivity : AppCompatActivity() {
    private val viewModel: StatsViewModel by viewModels {
        StatsViewModelFactory(QuizRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        viewModel.getUserStats()
        viewModel.stats.observe(this){
            Log.d("stats", it.toString())
        }
    }
}