package com.dicoding.consumerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.consumerapp.favorite.FavoriteFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FavoriteFragment())
            .addToBackStack(null)
            .commit()
    }
}