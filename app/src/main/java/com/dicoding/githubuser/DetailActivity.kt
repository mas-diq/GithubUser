package com.dicoding.githubuser

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.data.FavoriteUser
import com.dicoding.githubuser.data.FavoriteUserDao
import com.dicoding.githubuser.data.UserDatabase
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    //Room DB
    private lateinit var userDb: UserDatabase
    private lateinit var userDao: FavoriteUserDao
    private lateinit var binding: ActivityDetailBinding
    private var eventsUser: User? = null

    companion object {
        const val EXTRA_USER = "extra_user"
        var EXTRA_NAME = "extra_name"
        private val TAG = DetailActivity::class.java.simpleName

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        userDb = UserDatabase.getDatabase(this)!!
        userDao = userDb.favoriteUserDao()

        // Intent data from MainActivity
        eventsUser = intent.getParcelableExtra(EXTRA_USER)
        eventsUser?.let { getDetail(it.username) }

        val idd = eventsUser?.id

        // Return Back Listener
        val returnDetail: ImageView = binding.btnBack
        returnDetail.setOnClickListener(this)

        //tablayout
        val sectionsPagerAdapter =
            eventsUser?.username?.let {
                SectionsPagerAdapter(
                    this, username = it
                )
            }

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        //function check condition
        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = checkUSer(idd)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleButton.isChecked = true
                        isChecked = true
                    } else {
                        binding.toggleButton.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        //change condition when user is un favored
        binding.toggleButton.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                Toast.makeText(this, "$isChecked", Toast.LENGTH_SHORT).show()
                addToFavorite(
                    eventsUser?.avatar,
                    eventsUser?.username,
                    eventsUser?.name,
                    eventsUser?.location,
                    eventsUser?.repository,
                    eventsUser?.company,
                    eventsUser?.follower,
                    eventsUser?.following,
                    eventsUser?.id,
                    eventsUser?.type
                )
            } else {
                if (idd != null) {
                    removeFromFavorite(idd)
                }
            }
            binding.toggleButton.isChecked = isChecked
        }
    }


    private fun getDetail(username: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${username}"
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
                    val login = responseObject.getString("login")
                    val name = responseObject.getString("name")
                    val avatar = responseObject.getString("avatar_url")
                    val company = responseObject.getString("company")
                    val location = responseObject.getString("location")
                    val following = responseObject.getString("following")
                    val follower = responseObject.getString("followers")
                    val repository = responseObject.getString("public_repos")
                    val id = responseObject.getInt("id")
                    val data = User(
                        name = name,
                        company = company,
                        location = location,
                        follower = follower,
                        following = following,
                        username = login,
                        avatar = avatar,
                        repository = repository,
                        id = id
                    )

                    Glide.with(this@DetailActivity).load(data.avatar)
                        .apply(RequestOptions())
                        .into(binding.tvItemAvatar)
                    binding.tvItemName.text = data.name
                    binding.tvItemUserName.text = data.username
                    binding.tvFollower.text = data.follower
                    binding.tvFollowing.text = data.following
                    binding.tvCompany.text = data.company
                    binding.tvLocation.text = data.location
                    binding.tvRepository.text = data.repository

                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBack -> finish()
        }
    }

    //Insert user to favorite
    private fun addToFavorite(
        avatar: String?,
        username: String?,
        name: String?,
        location: String?,
        repository: String?,
        company: String?,
        follower: String?,
        following: String?,
        id: Int?,
        type: String?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                avatar ?: "",
                username ?: "",
                name ?: "",
                location ?: "",
                repository ?: "",
                company ?: "",
                follower ?: "",
                following ?: "",
                id ?: 0,
                type ?: ""
            )
            userDao.addUser(user)
        }
    }

    //check user if already exist
    private suspend fun checkUSer(id: Int?) = id?.let { userDao.checkUser(it) }

    //delete user
    private fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.deleteUser(id)
        }
    }

}

