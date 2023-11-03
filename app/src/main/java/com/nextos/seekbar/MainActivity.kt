package com.nextos.seekbar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var mSeekBar: CenterSeekBar? = null
    private var center = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        mSeekBar = findViewById<View>(R.id.seekbar) as CenterSeekBar

        findViewById<SeekBarView>(R.id.seekbar_view).setCenterModeEnable(true)
    }
//
//    fun reFlush(view: View?) {
//        center = !center
//        mSeekBar!!.setCenterModeEnable(center)
//    }
}