package com.rafsan.image_subtitlebutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.rafsan.image_subtitlebutton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.titleBtn.setTitleColor(ContextCompat.getColor(this, android.R.color.holo_blue_light))
        setContentView(binding.root)
    }
}