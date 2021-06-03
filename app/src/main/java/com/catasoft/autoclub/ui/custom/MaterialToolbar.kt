package com.catasoft.autoclub.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toolbar
import com.catasoft.autoclub.R

class MaterialToolbar : Toolbar {

    private var tvTitle: TextView? = null
    private var backImgBtn: ImageButton? = null

    private fun inflateView() {
        val view = View.inflate(context, R.layout.material_toolbar_view, this)
        tvTitle = view.findViewById(R.id.title_textview)
        backImgBtn = view.findViewById(R.id.imageButton4)
    }

    constructor(context: Context) : super(context) {
        inflateView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        inflateView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        inflateView()
    }

    fun setTitle(str: String) {
        this.tvTitle?.text = str
    }

    fun onBackPressed(callback: () -> Unit) {
        this.backImgBtn?.setOnClickListener {
            callback()
        }
    }
}