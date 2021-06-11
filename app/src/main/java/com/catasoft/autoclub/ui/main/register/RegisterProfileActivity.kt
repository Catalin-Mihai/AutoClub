package com.catasoft.autoclub.ui.main.register

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.ActivityRegisterProfileBinding
import com.catasoft.autoclub.ui.main.addmeet.*
import com.catasoft.autoclub.ui.util.BottomButtonsListener
import com.catasoft.autoclub.ui.util.BottomButtonsNavManager
import com.catasoft.autoclub.ui.util.GenericPageCollectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class RegisterProfileActivity: AppCompatActivity(), BottomButtonsListener {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterProfileBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomButtonsNavManager: BottomButtonsNavManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Timber.e("Register Activity!")

        //lifecycle setters
        binding.lifecycleOwner = this

        viewPager2 = binding.viewPager

        val fragmentList: ArrayList<Fragment> = arrayListOf(
            RegisterMyProfileFragment(),
            RegisterFinishFragment()
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
    }

    override fun onPrevPressed(currentPosition: Int) {
        viewPager2.setCurrentItem(currentPosition-1, true)
    }

    override fun onNextPressed(currentPosition: Int) {
        viewModel.validatePage(0)
    }

    override fun onFinishPressed(currentPosition: Int) {
        val mIcon = ResourcesCompat.getDrawable(resources, R.drawable.default_user_avatar, null)?.toBitmap()
        Timber.e(mIcon.toString())
        viewModel.registerUserInDatabase(mIcon)
    }

    override fun onPageChanged(currentPosition: Int) {
        super.onPageChanged(currentPosition)
        viewModel.liveValidationMessage.value = null
    }

    companion object {
        const val RESULT_REGISTERED = 1
        const val RESULT_NOT_REGISTERED = 2
    }
}