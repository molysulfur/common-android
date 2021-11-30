package com.awonar.app.ui.portfolio.adapter

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlin.math.max
import kotlin.math.min
import android.content.Context
import android.graphics.*

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.awonar.app.R
import timber.log.Timber


class PortfolioListItemTouchHelperCallback constructor(
    private val action: IPortfolioListItemTouchHelperCallback,
    val context: Context
) : ItemTouchHelper.Callback() {

    companion object {
        private const val buttonWidth = 300
        private const val textSize = 24f
    }

    enum class ItemState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }

    private var buttonShowedState: ItemState = ItemState.GONE
    private var swipeBack = false
    private var buttonInstance: RectF? = null
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null
    private var buttonList: ArrayList<SwipeButton> = arrayListOf()

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = makeMovementFlags(0, LEFT or RIGHT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = buttonShowedState != ItemState.GONE;
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        var newDX = dX
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ItemState.GONE) {
                if (buttonShowedState == ItemState.LEFT_VISIBLE) newDX =
                    max(newDX, buttonWidth.toFloat() * 2)
                if (buttonShowedState == ItemState.RIGHT_VISIBLE) newDX =
                    min(newDX, -buttonWidth.toFloat())
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    newDX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            } else {
                setTouchListener(
                    c,
                    recyclerView,
                    viewHolder,
                    newDX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        if (buttonShowedState == ItemState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            swipeBack =
                event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (dX < -buttonWidth) {
                    buttonShowedState = ItemState.RIGHT_VISIBLE
                } else if (dX > buttonWidth) {
                    buttonShowedState = ItemState.LEFT_VISIBLE;
                }
                if (buttonShowedState != ItemState.GONE) {
                    setTouchDownListener(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    setItemsClickable(recyclerView, false)
                }
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    0f,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                recyclerView.setOnTouchListener { v, event -> false }
                setItemsClickable(recyclerView, true)
                for (button in buttonList) {
                    if (button.onClick(
                            event?.x ?: -1f,
                            event?.y ?: -1f,
                            currentItemViewHolder?.absoluteAdapterPosition ?: -1
                        )
                    ) {
                        break
                    }
                }
                swipeBack = false
                buttonShowedState = ItemState.GONE
                currentItemViewHolder = null
            }
            false
        }
    }

    private fun setItemsClickable(
        recyclerView: RecyclerView,
        isClickable: Boolean
    ) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val buttonWidthWithoutPadding = (buttonWidth - 20).toFloat()
        val itemView: View = viewHolder.itemView
        val leftButton = RectF(
            itemView.left.toFloat(),
            itemView.top.toFloat(),
            itemView.left * 2 + buttonWidthWithoutPadding,
            itemView.bottom.toFloat()
        )
        val swipeButton = SwipeButton(
            text = "TP",
            imageRes = R.drawable.awonar_ic_tp, textSize,
            color = R.color.awonar_color_primary,
            rectF = leftButton,
            context = context
        ) { button, position ->
            action.onClick(position)
        }
        swipeButton.drawButton(c)
        buttonList.add(swipeButton)
        val left2Button = RectF(
            itemView.left * 2f,
            itemView.top.toFloat(),
            itemView.left * 2f + buttonWidthWithoutPadding,
            itemView.bottom.toFloat()
        )
        left2Button.left = 600f
        val swipeButton2 = SwipeButton(
            text = "SL",
            imageRes = R.drawable.awonar_ic_sl, textSize,
            color = R.color.awonar_color_orange,
            rectF = left2Button,
            context = context
        ) { button, position ->
            action.onClick(position)
        }
        swipeButton2.drawButton(c)
        buttonList.add(swipeButton2)
        val rightButton = RectF(
            itemView.right - buttonWidthWithoutPadding,
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat()
        )
        val swipeButton3 = SwipeButton(
            text = "Close",
            imageRes = R.drawable.awonar_ic_close, textSize,
            color = R.color.awonar_color_gray,
            rectF = rightButton,
            context = context
        ) { button, position ->
            action.onClick(position)
        }
        swipeButton3.drawButton(c)
        buttonList.add(swipeButton3)
        buttonInstance = null
        if (buttonShowedState === ItemState.LEFT_VISIBLE) {
            buttonInstance = leftButton
        } else if (buttonShowedState === ItemState.RIGHT_VISIBLE) {
            buttonInstance = rightButton
        }
    }

    fun onDraw(c: Canvas) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder!!)
        }
    }


    private class SwipeButton constructor(
        var text: String = "",
        private var imageRes: Int = 0,
        private var textSize: Float = 0f,
        private var color: Int = 0,
        private var rectF: RectF,
        private var context: Context,
        var onClick: ((SwipeButton, Int) -> Unit)? = null,
    ) {

        fun onClick(x: Float, y: Float, position: Int): Boolean {
            if (rectF.contains(x, y)) {
                onClick?.invoke(this, position)
                return true
            }
            return false
        }


        fun drawButton(c: Canvas) {
            val p = Paint()
            p.color = ContextCompat.getColor(context, color)
            c.drawRoundRect(rectF, 0f, 0f, p)
            val iconTp: Bitmap? =
                ContextCompat.getDrawable(context, imageRes)?.toBitmap()
            iconTp?.let {
                drawText(text, c, rectF, p, it)
            }
        }

        private fun drawText(text: String, c: Canvas, button: RectF, p: Paint, icon: Bitmap) {
            p.color = Color.WHITE
            p.isAntiAlias = true
            p.textSize = textSize
            val textWidth: Float = p.measureText(text)
            val bounds = Rect()
            p.getTextBounds(text, 0, text.length, bounds);
            val combinedHeight: Float = icon.height + 10f + bounds.height()
            c.drawBitmap(
                icon,
                button.centerX() - (icon.width / 2),
                button.centerY() - (combinedHeight / 2),
                null
            )
            c.drawText(
                text,
                button.centerX() - textWidth / 2,
                button.centerY() + combinedHeight / 2,
                p
            )
        }
    }
}