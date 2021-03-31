package com.junkakeno.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.junkakeno.movieapp.databinding.ActivityMainBinding
import com.junkakeno.movieapp.ui.fragments.MovieSearchFragment
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Timber
        if(BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        val movieSearchFragment = MovieSearchFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, movieSearchFragment, MovieSearchFragment::class.java.simpleName)
                .commitAllowingStateLoss()

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}