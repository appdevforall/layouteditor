package com.itsvks.layouteditor.adapters

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.itsvks.layouteditor.R
import com.itsvks.layouteditor.databinding.LayoutPaletteItemBinding
import com.itsvks.layouteditor.utils.InvokeUtil.getMipmapId
import com.itsvks.layouteditor.utils.InvokeUtil.getSuperClassName
import kotlin.math.abs

class PaletteListAdapter(
    private val drawerLayout: DrawerLayout
) : RecyclerView.Adapter<PaletteListAdapter.ViewHolder>() {
    private lateinit var tab: List<HashMap<String, Any>>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutPaletteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val widgetItem = tab[position]

        val binding = holder.binding

        binding.icon.setImageResource(getMipmapId(widgetItem["iconName"].toString()))
        binding.name.text = widgetItem["name"].toString()
        binding.className.text = getSuperClassName(widgetItem["className"].toString())

        with(binding.root) {
            setOnTouchListener(
                DragOrLongPressListener(
                    onLongPress = {
                        Toast.makeText(context, "Go to Help", Toast.LENGTH_SHORT).show()
                    },
                    onDrag = {
                        if (startDragAndDrop(null, View.DragShadowBuilder(this), widgetItem, 0)) {
                            drawerLayout.closeDrawers()
                        }
                    }
                )
            )
        }

        binding.root.animation = AnimationUtils.loadAnimation(
            holder.itemView.context, R.anim.project_list_animation
        )
    }

    override fun getItemCount(): Int {
        return tab.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitPaletteList(tab: List<HashMap<String, Any>>) {
        this.tab = tab
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: LayoutPaletteItemBinding) : RecyclerView.ViewHolder(binding.root)
}

private class DragOrLongPressListener(
    private val onLongPress: () -> Unit,
    private val onDrag: () -> Unit
) : View.OnTouchListener {
    private val longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()
    private val handler = Handler(Looper.getMainLooper())
    private var initialX = 0f
    private var initialY = 0f
    private var isDragging = false

    private val longPressRunnable = Runnable {
        if (!isDragging) {
            onLongPress()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val touchSlop = ViewConfiguration.get(v.context).scaledTouchSlop

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = event.x
                initialY = event.y
                isDragging = false
                handler.postDelayed({ longPressRunnable.run() }, longPressTimeout)
                true
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = abs(event.x - initialX)
                val dy = abs(event.y - initialY)

                if (!isDragging && (dx > touchSlop || dy > touchSlop)) {
                    handler.removeCallbacksAndMessages(null)
                    isDragging = true
                    onDrag()
                }
                true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacksAndMessages(null)
                isDragging = false
                true
            }

            else -> false
        }
    }
}
