package com.dicoding.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.favorite.FavoriteFragment
import com.dicoding.githubuser.fragment.ProfileFragment
import com.dicoding.githubuser.fragment.SettingFragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        internal val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView
    private var list: ArrayList<User> = arrayListOf()
    private lateinit var cardViewEventsAdapter: CardViewUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get user
        getUser("sidiq")

        // RecyclerCardView
        rvUsers = binding.rvUsers

        list.addAll(list)
        showRecyclerList()

        // Events Card Intent Move With Data
        cardViewEventsAdapter.onClickItem = {
            val intent = Intent(this, DetailActivity::class.java)
            //ngirim data
            intent.putExtra(DetailActivity.EXTRA_USER, it)
            startActivity(intent)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                getUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.MenuFavorite -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, FavoriteFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.MenuSetting -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.MenuProfile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ProfileFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            else -> return true
        }
    }

    private fun getUser(username: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=${username}"
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
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")
                    val users = arrayListOf<User>()

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
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
                    Toast.makeText(
                        this@MainActivity,
                        e.message, Toast.LENGTH_SHORT
                    ).show()
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
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecyclerList() {
        rvUsers.layoutManager = LinearLayoutManager(this)
        cardViewEventsAdapter = CardViewUserAdapter(list)
        rvUsers.adapter = cardViewEventsAdapter

        rvUsers.isNestedScrollingEnabled = false
        rvUsers.setHasFixedSize(false)
    }
}

