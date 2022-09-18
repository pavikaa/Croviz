package com.markopavicic.croviz.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.markopavicic.croviz.R
import com.markopavicic.croviz.databinding.ActivityStatsBinding
import com.markopavicic.croviz.model.data.Stats
import com.markopavicic.croviz.model.repository.QuizRepository
import com.markopavicic.croviz.viewmodel.StatsViewModel
import com.markopavicic.croviz.viewmodel.StatsViewModelFactory


class StatsActivity : AppCompatActivity() {
    private val viewModel: StatsViewModel by viewModels {
        StatsViewModelFactory(QuizRepository())
    }
    private lateinit var binding: ActivityStatsBinding
    private lateinit var stats: Stats

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getUserStats()
        viewModel.stats.observe(this) {
            stats = it
            setupGraphs()
        }
    }

    private fun setupGraphs() {
        val userColor = ContextCompat.getColor(this, R.color.light_blue)
        val globalColor = ContextCompat.getColor(this, R.color.navy_blue)
        var barChart = binding.scoreBarChart
        barChart.setTouchEnabled(false)
        var barEntryUser = BarEntry(1f, stats.userScore.toFloat())
        var barEntryGlobal = BarEntry(2f, stats.globalScore.toFloat())
        var userDataSet = BarDataSet(mutableListOf(barEntryUser), "Your score")
        userDataSet.color=userColor
        var globalDataSet = BarDataSet(mutableListOf(barEntryGlobal), "Global score")
        globalDataSet.color=globalColor
        var barData = BarData(listOf(userDataSet,globalDataSet))
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.xAxis.isEnabled = false
        barChart.invalidate()

        barChart = binding.totalAnswersBarChart
        barChart.setTouchEnabled(false)
        barEntryUser = BarEntry(1f, (stats.userCorrectAnswers+stats.userIncorrectAnswers).toFloat())
        barEntryGlobal = BarEntry(2f, (stats.globalCorrectAnswers+stats.globalIncorrectAnswers).toFloat())
        userDataSet = BarDataSet(mutableListOf(barEntryUser), "Your total answers")
        userDataSet.color=userColor
        globalDataSet = BarDataSet(mutableListOf(barEntryGlobal), "Global total answers")
        globalDataSet.color=globalColor
        barData = BarData(listOf(userDataSet,globalDataSet))
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.xAxis.isEnabled = false
        barChart.invalidate()

        barChart = binding.correctAnswersBarChart
        barChart.setTouchEnabled(false)
        barEntryUser = BarEntry(1f, (stats.userCorrectAnswers).toFloat())
        barEntryGlobal = BarEntry(2f, (stats.globalCorrectAnswers).toFloat())
        userDataSet = BarDataSet(mutableListOf(barEntryUser), "Your correct answers")
        userDataSet.color=userColor
        globalDataSet = BarDataSet(mutableListOf(barEntryGlobal), "Global correct answers")
        globalDataSet.color=globalColor
        barData = BarData(listOf(userDataSet,globalDataSet))
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.xAxis.isEnabled = false
        barChart.invalidate()

        barChart = binding.incorrectAnswersBarChart
        barChart.setTouchEnabled(false)
        barEntryUser = BarEntry(1f, (stats.userIncorrectAnswers).toFloat())
        barEntryGlobal = BarEntry(2f, (stats.globalIncorrectAnswers).toFloat())
        userDataSet = BarDataSet(mutableListOf(barEntryUser), "Your incorrect answers")
        userDataSet.color=userColor
        globalDataSet = BarDataSet(mutableListOf(barEntryGlobal), "Global incorrect answers")
        globalDataSet.color=globalColor
        barData = BarData(listOf(userDataSet,globalDataSet))
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.xAxis.isEnabled = false
        barChart.invalidate()

        barChart = binding.numQuizzesBarChart
        barChart.setTouchEnabled(false)
        barEntryUser = BarEntry(1f, (stats.userCompletedQuizzes).toFloat())
        barEntryGlobal = BarEntry(2f, (stats.globalCompletedQuizzes).toFloat())
        userDataSet = BarDataSet(mutableListOf(barEntryUser), "Your completed quizzes")
        userDataSet.color=userColor
        globalDataSet = BarDataSet(mutableListOf(barEntryGlobal), "Global completed quizzes")
        globalDataSet.color=globalColor
        barData = BarData(listOf(userDataSet,globalDataSet))
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.xAxis.isEnabled = false
        barChart.invalidate()

        barChart = binding.numQuestionsBarChart
        barChart.setTouchEnabled(false)
        barEntryUser = BarEntry(1f, (stats.userCompletedQuestions).toFloat())
        barEntryGlobal = BarEntry(2f, (stats.globalCompletedQuestions).toFloat())
        userDataSet = BarDataSet(mutableListOf(barEntryUser), "Your completed questions")
        userDataSet.color=userColor
        globalDataSet = BarDataSet(mutableListOf(barEntryGlobal), "Global completed questions")
        globalDataSet.color=globalColor
        barData = BarData(listOf(userDataSet,globalDataSet))
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.xAxis.isEnabled = false
        barChart.invalidate()
    }
}