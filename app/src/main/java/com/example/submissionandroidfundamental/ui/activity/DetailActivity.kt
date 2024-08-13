package com.example.submissionandroidfundamental.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.submissionandroidfundamental.R
import com.example.submissionandroidfundamental.data.response.ResponseDetailGithub
import com.example.submissionandroidfundamental.database.Favorite
import com.example.submissionandroidfundamental.databinding.DetailActivityBinding
import com.example.submissionandroidfundamental.ui.adapter.SectionsPagerAdapter
import com.example.submissionandroidfundamental.ui.model.DetailViewModel
import com.example.submissionandroidfundamental.ui.model.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding:DetailActivityBinding
    private val viewModel by viewModels<DetailViewModel>(){
        ViewModelFactory.getInstance(application)
    }
    private var name: String? = null
    private lateinit var favorite: Favorite

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)

        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = intent.getStringExtra(EXTRA_USERNAME)
        viewModel.detailUser(name.toString())
        viewModel.username.observe(this){
            user->
            if (user != null){
                setUserDetailData(user)
                favorite = Favorite(user.login?:"", user.avatarUrl)
            }
        }

        viewModel.errorMessage.observe(this){
            errorMessage ->
            if(!errorMessage.isNullOrEmpty()){
                showToast(errorMessage)
            }
        }
        viewModel.isLoading.observe(this){
            showLoading(it)
        }
        viewModel.checkFavorited(name.toString()).observe(this) { checked ->
            val btn = binding.btnFav

            if(checked != null){
                btn.setImageDrawable(ContextCompat.getDrawable(btn.context,R.drawable.ic_baseline_favorite))
                btn.setColorFilter(ContextCompat.getColor(btn.context, R.color.colorTintChecked))
            }else{
                btn.setImageDrawable(ContextCompat.getDrawable(btn.context,R.drawable.ic_favorite))
                btn.setColorFilter(ContextCompat.getColor(btn.context, R.color.colorTintUnchecked))
            }

            btn.setOnClickListener {
                if (checked != null) {
                    viewModel.deleteFavorite(favorite)
                } else {
                    viewModel.setFavorite(favorite)
                }
            }
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    private fun setUserDetailData(user:ResponseDetailGithub){
        binding.apply{
            tvUsername.text=user.name
            tvName.text=user.login
            tvFollowing.text = getString(R.string.following,user.following.toString())
            tvFollower.text = getString(R.string.followers,user.followers.toString())
            Glide.with(tvImage.context).load(user.avatarUrl).into(tvImage)
        }
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = name.toString()
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs,viewPager){
                tab,position-> tab.text =resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object{
       const val EXTRA_USERNAME =""
        private val TAB_TITLES = intArrayOf(
            R.string.followers_tabs,
            R.string.following_tabs
        )
    }
}
