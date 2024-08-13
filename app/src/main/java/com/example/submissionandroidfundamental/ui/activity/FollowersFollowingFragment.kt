package com.example.submissionandroidfundamental.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionandroidfundamental.R
import com.example.submissionandroidfundamental.data.response.ItemsItem
import com.example.submissionandroidfundamental.databinding.FragmentFollowersFollowingBinding
import com.example.submissionandroidfundamental.ui.adapter.UserAdapter
import com.example.submissionandroidfundamental.ui.model.DetailViewModel

class FollowersFollowingFragment : Fragment() {
    private lateinit var followerFollowingBinding: FragmentFollowersFollowingBinding
    private lateinit var viewModel: DetailViewModel

    private var username: String? = null
    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followerFollowingBinding = FragmentFollowersFollowingBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireActivity())
        followerFollowingBinding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(),layoutManager.orientation)
        followerFollowingBinding.rvFollow.addItemDecoration(itemDecoration)
        return followerFollowingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        if (position == 1) {
            viewModel.followerUser(username.toString())
            viewModel.followers.observe(viewLifecycleOwner) { follower->
                if(follower != null && follower.isNotEmpty()){
                    setUser(follower)
                }else{
                   showEmptyMessage(R.string.followers_null)
                }
            }
        } else {
            viewModel.followingUser(username.toString())
            viewModel.following.observe(viewLifecycleOwner) { following->
               if(following !== null && following.isNotEmpty()){
                   setUser(following)
               }else{
                   showEmptyMessage(R.string.following_null)
               }
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner){
                errorMessage ->
            if(!errorMessage.isNullOrEmpty()){
                showToast(errorMessage)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    private fun setUser(user: List<ItemsItem>) {
        val adapter = UserAdapter(requireActivity())
        adapter.submitList(user)
        this.followerFollowingBinding.rvFollow.adapter = adapter
    }


    private fun showEmptyMessage(message:Int){
        followerFollowingBinding.tvTextEmptyFragment.text = getString(message)
        followerFollowingBinding.tvTextEmptyFragment.visibility = View.VISIBLE
    }

    private fun showLoading(isLoading: Boolean) {
        followerFollowingBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val ARG_POSITION: String = "arg_position"
        const val ARG_USERNAME: String = "arg_username"
    }
}
