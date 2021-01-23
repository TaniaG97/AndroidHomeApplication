package com.example.androidhomeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moveDetailsBtn.setOnClickListener {
            moveToMoveDetailsActivity()
        }
    }

    fun moveToMoveDetailsActivity(){
        val intent = Intent(this, MovieDetailsActivity::class.java)
        startActivity(intent)
    }
}