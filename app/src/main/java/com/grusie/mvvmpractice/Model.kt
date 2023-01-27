package com.grusie.mvvmpractice

class Model {
    val password : MutableList<Int> = mutableListOf()

    fun inputPassword(i : Int){
        if(password.size < 4){
            password.add(i)
        }
    }

    fun checkPassword() : Boolean{
        var trueCount = 0
        var savePassword = mutableListOf<Int>(1,2,3,4)  //데이터베이스 대체

        for(i in 0 until savePassword.size) {
            if(savePassword[i] == password[i]){
                trueCount++
            }
        }
        return trueCount==4
    }
}