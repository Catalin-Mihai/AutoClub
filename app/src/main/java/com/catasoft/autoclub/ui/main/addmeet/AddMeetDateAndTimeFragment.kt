package com.catasoft.autoclub.ui.main.addmeet

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.catasoft.autoclub.databinding.FragmentAddMeetTimeBinding
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*


class AddMeetDateAndTimeFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentAddMeetTimeBinding
    private val viewModel: AddMeetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddMeetTimeBinding.inflate(layoutInflater)
        val rootView = binding.root

        //lifecycle setters
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.pickDateBtn.setOnClickListener {
            val now = Calendar.getInstance()
            val dpd: DatePickerDialog = DatePickerDialog.newInstance(
                this,
                now[Calendar.YEAR],  // Initial year selection
                now[Calendar.MONTH],  // Initial month selection
                now[Calendar.DAY_OF_MONTH] // Inital day selection
            )
            dpd.show(parentFragmentManager, "Datepickerdialog");
        }


        binding.pickTimeBtn.setOnClickListener {
            val now = Calendar.getInstance()
            val dpd: TimePickerDialog = TimePickerDialog.newInstance(
                this,
                now[Calendar.HOUR_OF_DAY],
                now[Calendar.MINUTE],
                true
            )
            dpd.show(parentFragmentManager, "Datepickerdialog");
        }

        return rootView
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        viewModel.setDate(year, monthOfYear, dayOfMonth)
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        viewModel.setTime(hourOfDay, minute)
    }
}