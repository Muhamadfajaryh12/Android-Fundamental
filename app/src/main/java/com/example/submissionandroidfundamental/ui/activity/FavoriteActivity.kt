package com.example.submissionandroidfundamental.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionandroidfundamental.R
import com.example.submissionandroidfundamental.data.response.ItemsItem
import com.example.submissionandroidfundamental.databinding.FavoriteActivityBinding
import com.example.submissionandroidfundamental.ui.adapter.UserAdapter
import com.example.submissionandroidfundamental.ui.model.FavoriteViewModel
import com.example.submissionandroidfundamental.ui.model.ViewModelFactory

class FavoriteActivity: AppCompatActivity() {

    private lateinit var binding:FavoriteActivityBinding
    private val viewModel by viewModels<FavoriteViewModel>(){
        ViewModelFactory.getInstance(application)
    }
    private val adapter: UserAdapter by lazy { UserAdapter(this@FavoriteActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FavoriteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycleView()
        binding.rvFavorite.adapter = adapter
        setupViewModel()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    private fun setupRecycleView(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager=layoutManager
        val itemDecoration = DividerItemDecoration(this,layoutManager.orientation)
        binding.rvFavorite.addItemDecoration(itemDecoration)
    }

    private fun setupViewModel(){
      viewModel.getAllFavorites().observe(this){
            users->
          if(users != null && users.isNotEmpty() ){
              val items = arrayListOf<ItemsItem>()
              users.map{
                  val item = ItemsItem(login=it.username ?: "", avatarUrl = it.avatarUrl.toString())
                  items.add(item)
              }
              adapter.submitList(items)
          }else{
              adapter.submitList(emptyList())
              binding.textFav.text=getString(R.string.text_fav)
              binding.textFav.visibility= View.VISIBLE
          }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}