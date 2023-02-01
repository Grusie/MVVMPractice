package com.grusie.mvvmpractice

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData

class ViewModel {
    var toastMessage = MutableLiveData<Int>()
    var checkPasswordMessage = ObservableField<Boolean>(false)

    val model = Model()

    fun clickNum(i: Int){
        toastMessage.value = i

        model.inputPassword(i)
        if(model.password.size == 4 && model.checkPassword()){
            checkPasswordMessage.set(true)
        }
    }
}