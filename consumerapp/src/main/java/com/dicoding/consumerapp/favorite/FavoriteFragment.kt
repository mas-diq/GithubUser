package com.dicoding.consumerapp.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.consumerapp.CardViewUserAdapter
import com.dicoding.consumerapp.User
import com.dicoding.consumerapp.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private lateinit var _binding: FragmentFavoriteBinding
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

        context?.let { viewModel.setFavoriteUser(it) }

        viewModel.getFav().observe(viewLifecycleOwner) {
            adapter.addUser(it)
        }
    }

    private fun showRecyclerList() {
        rvUsersFav.layoutManager = LinearLayoutManager(activity)
        adapter = CardViewUserAdapter(list)
        rvUsersFav.adapter = adapter
        rvUsersFav.isNestedScrollingEnabled = false
        rvUsersFav.setHasFixedSize(false)
    }
}