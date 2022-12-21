package com.fikri.netplix

import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fikri.netplix.core.custom_view.PaintView.Companion.colorList
import com.fikri.netplix.core.custom_view.PaintView.Companion.currentBrush
import com.fikri.netplix.core.custom_view.PaintView.Companion.pathList
import com.fikri.netplix.databinding.ActivityFreeDrawingBinding

class FreeDrawingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFreeDrawingBinding

    companion object {
        var path = Path()
        var paintBrush = Paint()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreeDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            cvRed.setOnClickListener {
                paintBrush.color = ContextCompat.getColor(this@FreeDrawingActivity, R.color.red)
                currentColor(paintBrush.color)
            }
            cvGreen.setOnClickListener {
                paintBrush.color = ContextCompat.getColor(this@FreeDrawingActivity, R.color.green)
                currentColor(paintBrush.color)
            }
            cvBlue.setOnClickListener {
                paintBrush.color = ContextCompat.getColor(this@FreeDrawingActivity, R.color.blue)
                currentColor(paintBrush.color)
            }
            cvEraser.setOnClickListener {
                pathList.clear()
                colorList.clear()
                path.reset()
            }
        }
    }

    private fun currentColor(color: Int) {
        currentBrush = color
        path = Path()
    }
}