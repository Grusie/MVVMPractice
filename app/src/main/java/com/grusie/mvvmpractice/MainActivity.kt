package com.grusie.mvvmpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.grusie.mvvmpractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    var model = Model()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.mainActivity = this
    }

    fun clickNum(i: Int){
        Toast.makeText(this, "$i 버튼이 클릭됨", Toast.LENGTH_SHORT).show()

        model.inputPassword(i)
        if(model.password.size == 4 && model.checkPassword()){
            binding.messageSuccess.visibility = View.VISIBLE
        }
    }
}