package com.apisov.percusoid.ui.instrumentdetails

import android.databinding.BindingAdapter
import com.shawnlin.numberpicker.NumberPicker

object NumberPickerValueBindings {

    @BindingAdapter("app:np_value")
    @JvmStatic
    fun setValue(numberPicker: NumberPicker, value: Int) {
        numberPicker.value = value
    }
}
