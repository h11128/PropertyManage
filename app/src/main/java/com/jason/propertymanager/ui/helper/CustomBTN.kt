package com.jason.propertymanager.ui.helper

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.CustomButtonTextNumberBinding

class CustomBTN(mContext: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(mContext, attrs) {
    private var _binding: CustomButtonTextNumberBinding? = null
    val binding get() = _binding!!
    private val imageView: ImageView
    private val textTitle: TextView
    private val textNumber: TextView
    private var activeColor: Int? = null
    private var negativeColor: ColorFilter? = null

    var number: Int = 0
        set(value) {
            field = value
            if (value > 0) {
                textNumber.visibility = View.VISIBLE
                textNumber.text = value.toString()
                activeColor?.let {
                    imageView.setColorFilter(it)
                    textNumber.background.colorFilter = PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN)
                    textTitle.setTextColor(it)
                }

            } else {
                textNumber.visibility = View.GONE
                negativeColor?.let {
                    imageView.colorFilter = it
                }
            }
        }

    init {
        inflate(mContext, R.layout.custom_button_text_number, this)
        _binding = CustomButtonTextNumberBinding.bind(this)
        imageView = binding.imageButtonBtn
        textTitle = binding.textBtnTitle
        textNumber = binding.textBtnNumber
        number = 0
        negativeColor = imageView.colorFilter
        mContext.obtainStyledAttributes(attrs, R.styleable.CustomBTN).apply {
            imageView.setImageDrawable(getDrawable(R.styleable.CustomBTN_buttonSrc))
            textTitle.text = getString(R.styleable.CustomBTN_buttonTitle)
            activeColor = getColor(R.styleable.CustomBTN_activeColor, R.color.darkGreen)
            recycle()
        }
    }

    fun setClickListener(clickListener: OnClickListener) {
        this.setOnClickListener(clickListener)
    }

}