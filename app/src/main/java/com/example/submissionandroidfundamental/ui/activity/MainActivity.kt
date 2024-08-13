package com.example.submissionandroidfundamental.ui.activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionandroidfundamental.R
import com.example.submissionandroidfundamental.data.response.ItemsItem
import com.example.submissionandroidfundamental.databinding.ActivityMainBinding
import com.example.submissionandroidfundamental.ui.SettingPreferences
import com.example.submissionandroidfundamental.ui.adapter.UserAdapter
import com.example.submissionandroidfundamental.ui.dataStore
import com.example.submissionandroidfundamental.ui.model.MainViewModel
import com.example.submissionandroidfundamental.ui.model.PreferenceViewModel
import com.example.submissionandroidfundamental.ui.model.PreferenceViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val preferenceViewModel = ViewModelProvider(this, PreferenceViewModelFactory(pref))[PreferenceViewModel::class.java]
        preferenceViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        setupRecyleView()

        setupViewModel()
    }

    private fun setupRecyleView(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this,layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.users.observe(this){listUser -> setUserData(listUser)}
        viewModel.errorMessage.observe(this){errorMessage->
            if(!errorMessage.isNullOrEmpty()){
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.isLoading.observe(this){
            showLoading(it)
        }
    }
    private var favoriteMenuItem: MenuItem? = null
    private var settingMenuItem: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        favoriteMenuItem = menu?.findItem(R.id.fav)
        settingMenuItem = menu?.findItem(R.id.setting)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnSearchClickListener {
            favoriteMenuItem?.isVisible = false
            settingMenuItem?.isVisible = false
        }

        searchView.setOnCloseListener {
            favoriteMenuItem?.isVisible = true
            settingMenuItem?.isVisible = true
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.findSearch(query)
                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun setUserData(listUser : List<ItemsItem>){
        val adapter = UserAdapter(this@MainActivity)
        adapter.submitList(listUser)
        binding.rvUser.adapter = adapter

        if(listUser.isEmpty()){
            binding.tvTextEmpty.text=getString(R.string.user_null)
            binding.tvTextEmpty.visibility=View.VISIBLE
        }else{
            binding.tvTextEmpty.visibility=View.INVISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav ->{
                startActivity(Intent(this,FavoriteActivity::class.java))
            }
            R.id.setting -> {
                startActivity(Intent(this, PreferenceActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}