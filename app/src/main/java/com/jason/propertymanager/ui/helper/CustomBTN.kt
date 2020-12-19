package com.jason.propertymanager.ui.helper

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.CustomButtonTextNumberBinding

class CustomBTN(mContext: Context, attrs: AttributeSet? = null) : ConstraintLayout(mContext, attrs) {
    private var _binding: CustomButtonTextNumberBinding? = null
    val binding get() = _binding!!
    private val imageView: ImageView
    private val textTitle: TextView
    private val textNumber: TextView
    var number: Int = 0
        set(value) {
            field = value
            if (value > 0) {
                textNumber.visibility = View.VISIBLE
                textNumber.text = value.toString()
                imageView.setColorFilter(this.resources.getColor(R.color.darkGreen))
                textTitle.setTextColor(this.resources.getColor(R.color.darkGreen))
            } else {
                textNumber.visibility = View.GONE
                imageView.setColorFilter(this.resources.getColor(R.color.black))
                textTitle.setTextColor(this.resources.getColor(R.color.black))
            }

        }

    init {
        inflate(mContext, R.layout.custom_button_text_number, this)
        _binding = CustomButtonTextNumberBinding.bind(this)
        imageView = binding.imageButtonBtn
        textTitle = binding.textBtnTitle
        textNumber = binding.textBtnNumber
        mContext.obtainStyledAttributes(attrs, R.styleable.CustomBTN).apply {
            imageView.setImageDrawable(getDrawable(R.styleable.CustomBTN_buttonSrc))
            textTitle.text = getString(R.styleable.CustomBTN_buttonTitle)
            recycle()
        }
    }

    fun setClickListener(clickListener: OnClickListener){
        this.setOnClickListener(clickListener)
    }

}