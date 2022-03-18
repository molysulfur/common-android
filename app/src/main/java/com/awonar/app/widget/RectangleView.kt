package com.awonar.app.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class RectangleView constructor(context: Context, val color: String) :
    View(context) {
    var paint: Paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        paint.color = Color.parseColor(color);
        paint.strokeWidth = 3f
        canvas?.drawRect(24f, 24f, 80f, 80f, paint);
    }

}