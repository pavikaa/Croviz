package com.markopavicic.croviz.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.markopavicic.croviz.databinding.FragmentQuizPreparationBinding
import com.markopavicic.croviz.utils.Constants

class QuizPreparationFragment : Fragment() {

    private var _binding: FragmentQuizPreparationBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizPreparationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val key = activity?.intent?.getStringExtra(Constants.QUIZ_ID_KEY)
        binding.key.text = key

    }
}