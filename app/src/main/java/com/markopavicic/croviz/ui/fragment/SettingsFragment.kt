package com.markopavicic.croviz.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.markopavicic.croviz.databinding.FragmentSettingsBinding
import com.markopavicic.croviz.utils.Prefs

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        if (Prefs.getThemeFromPrefs() == AppCompatDelegate.MODE_NIGHT_YES)
            binding.themeSwitch.isChecked = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.themeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
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