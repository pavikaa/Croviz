package com.markopavicic.croviz.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.markopavicic.croviz.R
import com.markopavicic.croviz.databinding.FragmentSettingsBinding
import com.markopavicic.croviz.model.repository.QuizRepository
import com.markopavicic.croviz.ui.activity.LoginActivity
import com.markopavicic.croviz.ui.activity.MainActivity
import com.markopavicic.croviz.utils.Prefs

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        if (Prefs.getThemeFromPrefs() == AppCompatDelegate.MODE_NIGHT_YES)
            binding.themeSwitch.isChecked = true

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        if (currentUser != null) {
            binding.btnClearStats.visibility = View.VISIBLE
            binding.btnSignIn.visibility = View.GONE
            binding.btnSignOut.visibility = View.VISIBLE
            binding.tvName.text = currentUser.displayName
        } else {
            binding.btnClearStats.visibility = View.GONE
            binding.btnSignIn.visibility = View.VISIBLE
            binding.btnSignOut.visibility = View.GONE
            binding.tvName.text = getString(R.string.logged_out_user)
        }
        binding.btnSignIn.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        binding.btnSignOut.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.sign_out))
                .setMessage(getString(R.string.sign_out_details))
                .setNegativeButton(getString(R.string.no)) { _, _ ->
                    // Respond to negative button press
                }
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    binding.btnClearStats.visibility = View.GONE
                    binding.btnSignIn.visibility = View.VISIBLE
                    binding.btnSignOut.visibility = View.GONE
                    binding.tvName.text = getString(R.string.logged_out_user)
                    mAuth.signOut()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    Toast.makeText(
                        context,
                        getString(R.string.sign_out_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .show()
        }
        binding.btnClearStats.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.clear_stats))
                .setMessage(getString(R.string.clear_stats_description))
                .setNegativeButton(getString(R.string.no)) { _, _ ->
                    // Respond to negative button press
                }
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    QuizRepository().clearStats()
                    Toast.makeText(
                        context,
                        getString(R.string.clear_stats_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .show()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setTheme(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                setTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setTheme(themeId: Int) {
        AppCompatDelegate.setDefaultNightMode(themeId)
        Prefs.setThemeToPrefs(themeId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}