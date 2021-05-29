package com.catasoft.autoclub.ui.main.car

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.catasoft.autoclub.R
import com.catasoft.autoclub.model.car.CarDetailsModel
import com.catasoft.autoclub.model.car.CarPhotoModel

class CarPhotoOverlayView constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var onDeleteClick: (CarPhotoModel) -> Unit = {}
    private var carOverlayDescriptionText: TextView? = null
    private var carOverlayDeleteButton: ImageView? = null

    init {
        val parent = View.inflate(context, R.layout.car_photo_overlay, this)
        carOverlayDeleteButton = parent.findViewById(R.id.carOverlayDeleteButton) as ImageView
        carOverlayDescriptionText = parent.findViewById(R.id.carOverlayDescriptionText) as TextView

        setBackgroundColor(Color.TRANSPARENT)
    }

    fun update(carPhotoModel: CarPhotoModel) {
        carOverlayDescriptionText?.text = carPhotoModel.description
        carOverlayDeleteButton?.setOnClickListener { onDeleteClick(carPhotoModel) }
    }
}
