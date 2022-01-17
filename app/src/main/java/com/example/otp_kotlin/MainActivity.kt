package com.example.otp_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit  var mobile_no: EditText
    lateinit var send_button: LinearLayout
    lateinit  var send_txt_button: TextView
    lateinit var error_mobile_txt: TextInputLayout
    lateinit var send_progressbar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mobile_no = findViewById(R.id.mobile_no)
        error_mobile_txt = findViewById(R.id.error_mobile_txt)
        send_button = findViewById(R.id.send_button)
        send_txt_button = findViewById(R.id.send_txt_button)
        send_progressbar = findViewById(R.id.send_progressbar)



        send_button.setOnClickListener {

            try {

                //                getting inputdata from textbox
                val mobile = mobile_no.text.toString().trim { it <= ' ' }
                if (mobile.isEmpty()) {
                    error_mobile_txt.error = "Please Enter Mobile Number."
                } else {

                    //                      hiding progresses bar and  change textview ,disable button
                    send_progressbar.visibility = View.VISIBLE
                    send_txt_button.text = "Please Wait"
                    send_button.isEnabled = false
                    val intent = Intent(this@MainActivity, Verification::class.java)
                    intent.putExtra("mobile", mobile)
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }


        }


    }
}