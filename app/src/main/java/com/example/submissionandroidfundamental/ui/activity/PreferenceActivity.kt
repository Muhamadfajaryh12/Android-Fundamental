package com.example.submissionandroidfundamental.ui.activity

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.submissionandroidfundamental.databinding.PreferenceActivityBinding
import com.example.submissionandroidfundamental.ui.SettingPreferences
import com.example.submissionandroidfundamental.ui.dataStore
import com.example.submissionandroidfundamental.ui.model.PreferenceViewModel
import com.example.submissionandroidfundamental.ui.model.PreferenceViewModelFactory

class PreferenceActivity:AppCompatActivity() {

    private lateinit var binding: PreferenceActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PreferenceActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = SettingPreferences.getInstance(application.dataStore)
        val preferenceViewModel = ViewModelProvider(
            this,
            PreferenceViewModelFactory(pref)
        )[PreferenceViewModel::class.java]
        preferenceViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            preferenceViewModel.saveThemeSetting(isChecked)
        }
        supportActionBar?.title = "Setting"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}