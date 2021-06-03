package com.catasoft.autoclub.ui.main.addmeet

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.ActivityAddMeetBinding
import com.catasoft.autoclub.ui.util.GenericPageCollectionAdapter
import com.catasoft.autoclub.ui.util.BottomButtonsListener
import com.catasoft.autoclub.ui.util.BottomButtonsNavManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@FlowPreview
@AndroidEntryPoint
class AddMeetActivity : AppCompatActivity(), BottomButtonsListener {


    private val viewModel: AddMeetViewModel by viewModels()
    private lateinit var binding: ActivityAddMeetBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomButtonsNavManager: BottomButtonsNavManager

    private val timer = object: CountDownTimer(4000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

        }
        override fun onFinish() {
            viewModel.liveValidationMessage.value = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddMeetBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //lifecycle setters
        binding.lifecycleOwner = this

        viewPager2 = binding.viewPager

        val fragmentList: ArrayList<Fragment> = arrayListOf(
            AddMeetLocationFragment(),
            AddMeetDescriptionFragment(),
            AddMeetDateAndTimeFragment(),
            AddMeetSummaryFragment()
        )
        viewPager2.adapter = GenericPageCollectionAdapter(this, fragmentList)
        //Programmatically swipe to pages to let the user know if it's something wrong with the input on the current page
        viewPager2.isUserInputEnabled = false

        bottomButtonsNavManager = BottomButtonsNavManager(
            binding.btnPrev, binding.btnNext, viewPager2, fragmentList.size, this,
            resources.getString(R.string.add_car_prev_button_text), resources.getString(R.string.add_car_next_button_text), resources.getString(
                R.string.add_car_finish_button_text))

        viewModel.liveValidationState.observe(this, {
            if(it == true){
                when(bottomButtonsNavManager.getPageState()){
                    BottomButtonsNavManager.FIRST_PAGE, BottomButtonsNavManager.MIDDLE_PAGE -> {
                        //Here we just wanted to know if we can go further with the creation wizard
                        viewPager2.setCurrentItem(viewPager2.currentItem+1, true)
                    }
                }
            }
        })

        viewModel.liveMeetSaved.observe(this, {
            if(it){
                finish()
            }
        })

        viewModel.liveValidationMessage.observe(this, {
            timer.cancel()
            timer.start()
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

        //Save the new meet
        viewModel.saveMeet()

        Snackbar.make(binding.root, resources.getText(R.string.saving), Snackbar.LENGTH_SHORT).show()

        //disable save button
        binding.btnNext.isClickable = false
    }

    override fun onPageChanged(currentPosition: Int) {
        super.onPageChanged(currentPosition)

        timer.cancel()
        viewModel.liveValidationMessage.value = null
    }

}