package com.dicoding.githubuser.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuser.CardViewUserAdapter
import com.dicoding.githubuser.User
import com.dicoding.githubuser.data.FavoriteUser
import com.dicoding.githubuser.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private lateinit var _binding: FragmentFavoriteBinding

    //    private val binding get() = _binding
    private lateinit var viewModel: FavoriteViewModel

    private lateinit var rvUsersFav: RecyclerView
    private lateinit var adapter: CardViewUserAdapter
    private var list = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //recyclerview
        rvUsersFav = _binding.rv
        showRecyclerList()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(FavoriteViewModel::class.java)
        viewModel.getFav()?.observe(viewLifecycleOwner) {
            val usrList = addList(it)
            adapter.addUser(usrList)
        }
    }

    private fun addList(users: List<FavoriteUser>): ArrayList<User> {
        val listUser = ArrayList<User>()
        for (user in users) {
            val list = User(
                user.avatar,
                user.username,
                user.name,
                user.location,
                user.repository,
                user.company,
                user.follower,
                user.following,
                user.id,
                user.type,
            )
            listUser.add(list)
        }
        return listUser
    }

    private fun showRecyclerList() {
        rvUsersFav.layoutManager = LinearLayoutManager(activity)
        adapter = CardViewUserAdapter(list)
        rvUsersFav.adapter = adapter
        rvUsersFav.isNestedScrollingEnabled = false
        rvUsersFav.setHasFixedSize(false)
    }
}