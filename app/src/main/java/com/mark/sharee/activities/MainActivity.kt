package com.mark.sharee.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.sharee.R
import com.mark.sharee.core.ShareeActivity
import com.mark.sharee.fragments.MovieViewModel
import com.mark.sharee.screens.MovieScreen
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ShareeActivity() {

    private val movieViewModel: MovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.i("mark - onCreate")

    }

    override fun onResume() {
        super.onResume()
        Timber.d("mark - onResume")
        navigator.takeActivity(this)
        navigator.add(MovieScreen())
    }

    override fun onPause() {
        super.onPause()
        navigator.dropActivity()
    }


}
