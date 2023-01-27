package com.grusie.mvvmpractice

class Presenter(var viewInterface: ViewInterface) {
    val model = Model()

    fun clickNum(i: Int){
        viewInterface.ToastMessage(i)

        model.inputPassword(i)
        if(model.password.size == 4 && model.checkPassword()){
            viewInterface.checkPasswordMessage()
        }
    }
}