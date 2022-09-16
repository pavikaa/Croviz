package com.markopavicic.croviz.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.markopavicic.croviz.R
import com.markopavicic.croviz.databinding.ActivityQuizCreationBinding
import com.markopavicic.croviz.utils.Prefs

class QuizCreationActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityQuizCreationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_quiz_creation)

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)

        if (Prefs.checkIfFirstRun())
            Prefs.firstRun()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_quiz_creation)
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }
}