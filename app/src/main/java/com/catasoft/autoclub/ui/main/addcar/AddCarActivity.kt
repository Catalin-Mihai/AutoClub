package com.catasoft.autoclub.ui.main.addcar

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.ActivityAddCarBinding
import com.catasoft.autoclub.ui.util.BottomButtonsListener
import com.catasoft.autoclub.ui.util.BottomButtonsNavManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

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
