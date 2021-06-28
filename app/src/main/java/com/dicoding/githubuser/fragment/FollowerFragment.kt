package com.dicoding.githubuser.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuser.BuildConfig
import com.dicoding.githubuser.CardViewUserAdapter
import com.dicoding.githubuser.DetailActivity
import com.dicoding.githubuser.DetailActivity.Companion.EXTRA_NAME
import com.dicoding.githubuser.User
import com.dicoding.githubuser.databinding.FragmentFollowerBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowerFragment : Fragment() {

    companion object {
        internal val TAG = DetailActivity::class.java.simpleName
    }

    private var _binding: FragmentFollowerBinding? = null
    private lateinit var rvUsers: RecyclerView
    private lateinit var cardViewEventsAdapter: CardViewUserAdapter
    private var list: ArrayList<User> = arrayListOf()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val getUser = arguments?.getString(EXTRA_NAME)
        if (getUser != null) {
            Log.d("follower Fragment", getUser)
        }
        // RecyclerCardView
        rvUsers = binding.rvUsersFollower
        if (getUser != null) {
            showRecyclerList()
            getFollower(getUser)
        }
    }

    private fun getFollower(username: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${username}/followers"
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONArray(result)
                    val users = arrayListOf<User>()

                    for (i in 0 until responseObject.length()) {
                        val item = responseObject.getJSONObject(i)
                        val login = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val id = item.getInt("id")
                        val type = item.getString("type")
                        val data = User(
                            username = login,
                            avatar = avatar,
                            id = id,
                            type = type
                        )
                        users.add(data)
                    }
                    cardViewEventsAdapter.addUser(users)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecyclerList() {
        rvUsers.layoutManager = LinearLayoutManager(requireContext())
        cardViewEventsAdapter = CardViewUserAdapter(list)
        rvUsers.adapter = cardViewEventsAdapter

        rvUsers.isNestedScrollingEnabled = false
        rvUsers.setHasFixedSize(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}