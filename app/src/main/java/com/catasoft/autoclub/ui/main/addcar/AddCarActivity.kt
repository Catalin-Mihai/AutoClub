package com.catasoft.autoclub.ui.main.addcar

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.ActivityAddCarBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

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

@AndroidEntryPoint
@FlowPreview
class AddCarActivity: AppCompatActivity(), BottomButtonsListener {

    private lateinit var binding: ActivityAddCarBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomButtonsNavManager: BottomButtonsNavManager
    private val viewModel: AddCarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewPager2 = binding.viewPager

        val fragmentList = arrayListOf(
            AddCarInfoFragment(),
            AddCarNumberPlateFragment(),
            AddCarSummaryFragment()
        )
        viewPager2.adapter = AddCarPageCollectionAdapter(this, fragmentList)
        //Programmatically swipe to pages to let the user know if it's something wrong with the input on the current page
        viewPager2.isUserInputEnabled = false

        bottomButtonsNavManager = BottomButtonsNavManager(
            binding.btnPrev, binding.btnNext, viewPager2, fragmentList.size, this,
            resources.getString(R.string.add_car_prev_button_text), resources.getString(R.string.add_car_next_button_text), resources.getString(R.string.add_car_finish_button_text))

        viewModel.currentPageValidState.observe(this, {
            if(it == true){
                when(bottomButtonsNavManager.getPageState()){
                    BottomButtonsNavManager.MIDDLE_PAGE, BottomButtonsNavManager.FIRST_PAGE -> {
                        //Here we just wanted to know if we can go further with the creation wizard
                        viewPager2.setCurrentItem(viewPager2.currentItem+1, true)
                    }
                }
            }
        })

        viewModel.carSavedState.observe(this, {
            if(it == true){
                //Now the car has been successfully saved
                //Can't go back or TODO: display a new confirmation page
                Timber.e("Masina a fost salvata boss!")
                finish()
            }
            else{
                Snackbar.make(binding.root, resources.getText(R.string.generic_error), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun onPrevPressed(currentPosition: Int) {
        Timber.e("Prev")
        //Can go back without problems, even if the input is bad.
        viewPager2.setCurrentItem(currentPosition-1, true)
    }

    override fun onNextPressed(currentPosition: Int) {
        Timber.e("Next")

        //Here we must be sure the input is ok. Can't go further unless the input is good
        //So check the input and wait for the livedata to come with the state
        viewModel.validatePage(currentPosition)
    }

    override fun onFinishPressed(currentPosition: Int) {
        Timber.e("Finish")

        //Save the car. If we are at the summary step, we know that the input is good
        viewModel.saveCarToDatabase()

        Snackbar.make(binding.root, resources.getText(R.string.saving), Snackbar.LENGTH_SHORT).show()

        //disable save button
        binding.btnNext.isClickable = false
    }

}
