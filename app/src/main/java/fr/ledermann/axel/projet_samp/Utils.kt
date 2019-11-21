package fr.ledermann.axel.projet_samp

import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.GradientDrawable
import android.widget.Button

const val BASE_BTN_COLOR_START = 0xff009999.toInt()
const val BASE_BTN_COLOR_END = 0xff00cccc.toInt()
const val SELECTED_BTN_COLOR_START = 0xff006666.toInt()
const val SELECTED_BTN_COLOR_END = 0xff009999.toInt()
const val DISABLED_BTN_COLOR_START = 0xffa6a6a6.toInt()
const val DISABLED_BTN_COLOR_END = 0xffbfbfbf.toInt()
const val CORRECT_BTN_COLOR_START = 0xff29a329.toInt()
const val CORRECT_BTN_COLOR_END = 0xff33cc33.toInt()

const val SELECTED_LANGUAGE = "language"
const val IS_SHOWING_ANSWERS = "is_showing_answers"

class Utils {
    companion object {
        fun changeGradientBtnColor(btn : Button, startColor : Int, endColor : Int) {
            val drawableContainerState = btn.background.constantState as DrawableContainer.DrawableContainerState
            val children = drawableContainerState.children
            val drawable = children[0] as GradientDrawable
            drawable.colors = intArrayOf(startColor, endColor)
        }
    }
}