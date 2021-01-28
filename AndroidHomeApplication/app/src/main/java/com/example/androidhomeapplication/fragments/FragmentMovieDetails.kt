package com.example.androidhomeapplication.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.androidhomeapplication.App
import com.example.androidhomeapplication.R
import kotlinx.android.synthetic.main.fragment_movie_details.*
import ru.terrakok.cicerone.Router

class FragmentMovieDetails : Fragment(R.layout.fragment_movie_details) {
    lateinit var router: Router

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        router = App.INSTANCE?.getRouter()!!
        text_back.setOnClickListener {
            router.exit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentMovieDetails()
    }

}