package gst.trainingcourse.firebasedemo.activity

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import gst.trainingcourse.firebasedemo.R

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

    }


}


