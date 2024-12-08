package com.kk.util.subtitleimagestitching

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kk.util.subtitleimagestitching.databinding.ActivityMainBinding
import com.kk.util.subtitleimagestitching.feature.imagestitching.SubtitleImageStitchingFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SubtitleImageStitchingFragment())
                .commit()
        }
    }
}