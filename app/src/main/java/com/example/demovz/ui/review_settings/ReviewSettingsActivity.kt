package com.example.demovz.ui.review_settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demovz.R
import com.example.demovz.databinding.ActivityMainBinding
import com.example.demovz.databinding.ActivityReviewSettingsBinding

class ReviewSettingsActivity : AppCompatActivity() {
    private var binding: ActivityReviewSettingsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewSettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {
            appBar.txtTitle.text= getString(R.string.review_settings)
        }



    }
}