package com.catasoft.autoclub.ui.util

import android.widget.Button
import androidx.viewpager2.widget.ViewPager2

interface BottomButtonsListener{
    fun onPrevPressed(currentPosition: Int)
    fun onNextPressed(currentPosition: Int)
    fun onFinishPressed(currentPosition: Int)
}

//TODO: Convert la clasa builder unde se specifica textele de inceput, intermediare si finale (Inainte, Inapoi, Finalizeaza)
open class BottomButtonsNavManager(
    private val prevButton: Button,
    private val nextButton: Button,
    viewPager2: ViewPager2,
    pagesCount: Int,
    private val listener: BottomButtonsListener,
    prevButtonText: String = "Inapoi",
    private var nextButtonText: String = "Inainte",
    private var finishButtonText: String = "Gata"
){
    private var currentPageNumber: Int = 0
    private var lastPageNumber: Int = 0

    private var pageState = -1

    companion object{
        const val FIRST_PAGE = 0
        const val MIDDLE_PAGE = 1
        const val LAST_PAGE = 2
    }

    init {
        currentPageNumber = viewPager2.currentItem

        //The page count starts from 0. Subtract 1 to find the last position
        lastPageNumber = pagesCount - 1

        //Set buttons text
        prevButton.text = prevButtonText
        nextButton.text = nextButtonText

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                currentPageNumber = position
                onPageChanged(position)
            }
        })

        //Manually trigger the onPageChanged event to synchronize the buttons for the first run
        //The viewpager might start with another current page other than the first page.
        onPageChanged(currentPageNumber)

        nextButton.setOnClickListener {
            if(pageState == LAST_PAGE)
            //The last page next button is the finish one
                listener.onFinishPressed(currentPageNumber)
            else
                listener.onNextPressed(currentPageNumber)
        }

        prevButton.setOnClickListener {
            listener.onPrevPressed(currentPageNumber)
        }
    }

    fun getPageState() = pageState

    private fun showAndClickable(button: Button){
        button.isClickable = true
        button.visibility = Button.VISIBLE
    }

    private fun hideAndNotClickable(button: Button){
        button.visibility = Button.INVISIBLE
        button.isClickable = false
    }

    private fun onPageChanged(position: Int){
        if(position != lastPageNumber){
            nextButton.text = nextButtonText
        }

        when(position){
            0 -> {
                showAndClickable(nextButton)
                hideAndNotClickable(prevButton)
                pageState = FIRST_PAGE
            }
            lastPageNumber -> {
                showAndClickable(nextButton)
                showAndClickable(prevButton)
                nextButton.text = finishButtonText
                pageState = LAST_PAGE
            }
            else -> {
                showAndClickable(nextButton)
                showAndClickable(prevButton)
                pageState = MIDDLE_PAGE
            }
        }
    }
}