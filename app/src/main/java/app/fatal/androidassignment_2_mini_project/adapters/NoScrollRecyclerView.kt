package app.fatal.androidassignment_2_mini_project.adapters

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class NoScrollRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs)  {

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // Disable touch event for scrolling
        return false
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        // Disable intercepting touch events for scrolling
        return false
    }

}