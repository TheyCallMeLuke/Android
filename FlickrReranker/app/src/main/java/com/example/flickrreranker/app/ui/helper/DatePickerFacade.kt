package com.example.flickrreranker.app.ui.helper

import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker

object DatePickerFacade {

    private val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select date")
        .setCalendarConstraints(
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build()
        )
        .build()

    fun show(fragment: Fragment) {
        datePicker.show(fragment.parentFragmentManager, null)
    }

    fun addOnPositiveButtonClickListener(callback: (Long) -> Unit) {
        datePicker.addOnPositiveButtonClickListener { callback(it) }
    }
}