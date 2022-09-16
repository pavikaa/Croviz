package com.markopavicic.croviz.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.markopavicic.croviz.R
import com.markopavicic.croviz.databinding.FragmentQuizCreationDetailsBinding

class QuizCreationDetailsFragment : Fragment() {

    private var _binding: FragmentQuizCreationDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var quizDetails: QuizDetails


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizCreationDetailsBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddQuestionsQuizCreation.setOnClickListener {
            if (getInputs()) {
                val action = QuizCreationDetailsFragmentDirections
                    .actionQuizCreationDetailsFragmentToQuizCreationQuestionsFragment(
                        quizDetails.name,
                        quizDetails.category,
                        quizDetails.hideCreator
                    )
                navController.navigate(action)
            }
        }
    }

    private fun getInputs(): Boolean {
        val tfName = binding.tfQuizNameCreation
        if (TextUtils.isEmpty(tfName.editText?.text)) {
            tfName.error = "Required*"
            return false
        } else {
            tfName.error = ""
        }
        val quizName = tfName.editText?.text.toString()
        val checkedButtonId = binding.bgCategoryCreation.checkedButtonId
        var category = getCategory(checkedButtonId)
        val hideCreator = binding.chkAnonQuizCreation.isChecked
        quizDetails = QuizDetails(quizName, category, hideCreator)
        return true
    }

    private fun getCategory(checkedButtonId: Int): String {
        when (checkedButtonId) {
            R.id.btn_check_general_knowledge -> {
                return "general_knowledge"
            }
            R.id.btn_check_music -> {
                return "music"
            }
            R.id.btn_check_tv -> {
                return "tv"
            }
            R.id.btn_check_science -> {
                return "science"
            }
            R.id.btn_check_history -> {
                return "history"
            }
            R.id.btn_check_geography -> {
                return "geography"
            }
            else -> {
                return "sport"
            }
        }
    }
}

data class QuizDetails(
    val name: String,
    val category: String,
    val hideCreator: Boolean
)