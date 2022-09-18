package com.markopavicic.croviz.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.markopavicic.croviz.R
import com.markopavicic.croviz.adapter.QuizAdapter
import com.markopavicic.croviz.databinding.FragmentHomeBinding
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.model.repository.QuizRepository
import com.markopavicic.croviz.ui.activity.EndlessQuizActivity
import com.markopavicic.croviz.ui.activity.QuizActivity
import com.markopavicic.croviz.ui.activity.QuizCreationActivity
import com.markopavicic.croviz.ui.activity.StatsActivity
import com.markopavicic.croviz.utils.Constants
import com.markopavicic.croviz.viewmodel.QuizViewModel
import com.markopavicic.croviz.viewmodel.QuizViewModelFactory


class HomeFragment : Fragment() {
    private val viewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(QuizRepository())
    }

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var quizRecyclerView: RecyclerView
    private lateinit var allQuizzes: MutableList<Quiz>

    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        quizRecyclerView = binding.rvQuiz
        viewModel.getAllQuizzes()
        return binding.root
    }

    private fun launchStatsActivity() {
        val intent = Intent(requireContext(), StatsActivity::class.java)
        startActivity(intent)
    }

    private fun launchEndlessQuiz() {
        val intent = Intent(requireContext(), EndlessQuizActivity::class.java)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener { view ->
            val modalBottomSheet = ModalBottomSheet()
            modalBottomSheet.show(childFragmentManager, ModalBottomSheet.TAG)
        }
        binding.cardEndlessFragmentHome.setOnClickListener {
            launchEndlessQuiz()
        }
        binding.cardStatsFragmentHome.setOnClickListener {
            launchStatsActivity()
        }
        quizRecyclerView = binding.rvQuiz

        if (user != null) {
            binding.cardStatsFragmentHome.setOnClickListener {
                launchStats()
            }
        } else {
            binding.cardStatsFragmentHome.visibility = View.GONE
        }

        viewModel.allQuizzes.observe(viewLifecycleOwner) { allQuizzes ->
            this.allQuizzes = allQuizzes
            setupRecyclerView(allQuizzes.toList())
            quizFilter()
        }
    }

    private fun launchStats() {
        if (user != null) {
            val intent = Intent(context, StatsActivity::class.java)
            startActivity(intent)
        } else
            Toast.makeText(
                context,
                "You need to be signed in to save your stats",
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun quizFilter() {
        binding.bgCategory.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_check_general_knowledge -> {
                        setupRecyclerView(allQuizzes.filter { it.quizCategory == "general_knowledge" })
                    }
                    R.id.btn_check_music -> {
                        setupRecyclerView(allQuizzes.filter { it.quizCategory == "music" })
                    }
                    R.id.btn_check_tv -> {
                        setupRecyclerView(allQuizzes.filter { it.quizCategory == "tv" })
                    }
                    R.id.btn_check_science -> {
                        setupRecyclerView(allQuizzes.filter { it.quizCategory == "science" })
                    }
                    R.id.btn_check_history -> {
                        setupRecyclerView(allQuizzes.filter { it.quizCategory == "history" })
                    }
                    R.id.btn_check_geography -> {
                        setupRecyclerView(allQuizzes.filter { it.quizCategory == "geography" })
                    }
                    R.id.btn_check_sport -> {
                        setupRecyclerView(allQuizzes.filter { it.quizCategory == "sport" })
                    }
                    else -> {
                        setupRecyclerView(allQuizzes)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(allQuizzes: List<Quiz>) {
        if (allQuizzes.isNotEmpty()) {
            quizRecyclerView.visibility = View.VISIBLE
            binding.tvNoQuizzes.visibility = View.GONE
            context?.let { quizRecyclerView.adapter = QuizAdapter(it, allQuizzes) }
        } else {
            quizRecyclerView.visibility = View.GONE
            binding.tvNoQuizzes.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ModalBottomSheet : BottomSheetDialogFragment() {
    private val user = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.modal_bottom_sheet_content, container, false)

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (user != null) {
            view.findViewById<MaterialButton>(R.id.btn_new_quiz).setOnClickListener {
                launchNewQuiz()
            }
        } else {
            view.findViewById<MaterialButton>(R.id.btn_new_quiz).visibility = View.GONE
        }
        view.findViewById<MaterialButton>(R.id.btn_scan_qr).setOnClickListener {
            launchScanQr()
        }
    }

    private fun launchScanQr() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan quiz QR code")
        options.setCameraId(0) // Use a specific camera of the device
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        barcodeLauncher.launch(options)
    }

    private fun launchNewQuiz() {
        val intent = Intent(context, QuizCreationActivity::class.java)
        startActivity(intent)
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Snackbar.make(requireView(), "Cancelled", Snackbar.LENGTH_LONG)
                .show()
        } else {
            val intent = Intent(context, QuizActivity::class.java)
            intent.putExtra(Constants.QUIZ_ID_KEY, result.contents)
            startActivity(intent)
        }
    }
}