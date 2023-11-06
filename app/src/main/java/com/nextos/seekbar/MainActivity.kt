package com.nextos.seekbar

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.nextos.seekbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        binding.vSeekbar.setAnimProgress(-8f)

        binding.vSeekbar.setOnSeekbarChangedListener(object : OnSeekbarChangedListener {
            override fun onSeekbarChanged(progress: Float) {
                Log.d("MainActivity", "onSeekbarChanged:$progress")
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        binding.vSeekbar.onKeyDown(keyCode, event)
        return super.onKeyDown(keyCode, event)
    }
}