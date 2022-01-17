package com.example.otp_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class Verification : AppCompatActivity() {

    lateinit var resend: TextView

    lateinit var otp_text: EditText

    lateinit var otp: String

    lateinit var confirm_button: LinearLayout

    lateinit var confirm_txt_button: TextView

    lateinit var confirm_progressbar: ProgressBar

    lateinit var error_email_txt: TextInputLayout

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        resend = findViewById(R.id.resend)
        otp_text = findViewById(R.id.otp_text)
        confirm_button = findViewById(R.id.confirm_button)
        confirm_txt_button = findViewById(R.id.confirm_txt_button)
        confirm_progressbar = findViewById(R.id.confirm_progressbar)
        error_email_txt = findViewById(R.id.error_email_txt)


        val intent = intent
        val mob = intent.getStringExtra("mobile")


        initiate_otp(mob)


        confirm_button.setOnClickListener {

//                getting inputdata from textbox


//                getting inputdata from textbox
            val otp_txt = otp_text.text.toString()

            if (otp_txt.isEmpty()) {
                error_email_txt.error = "Enter OTP"
            } else {

//                hiding progresses bar and  change textview ,disable button
                confirm_progressbar.visibility = View.VISIBLE
                confirm_txt_button.text = "Please Wait"
                confirm_button.isEnabled = false
                val credential = PhoneAuthProvider.getCredential(otp, otp_txt)
                signInWithPhoneAuthCredential(credential)
            }
        }





    }

    private fun initiate_otp(mob: String?) {

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber("+91 $mob") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                    super.onCodeSent(s, forceResendingToken)
                    otp = s
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(applicationContext, mob + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            ) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(phoneAuthCredential: PhoneAuthCredential) {

        firebaseAuth.signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {


                    Toast.makeText(
                        this,
                        "OTP Verified Successfully",
                        Toast.LENGTH_SHORT
                    ).show()



                    confirm_button.isEnabled = true
                    confirm_progressbar.visibility = View.GONE
                    confirm_txt_button.text = "Verify"

                } else {
                    Toast.makeText(applicationContext, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    confirm_button.isEnabled = true
                    confirm_progressbar.visibility = View.GONE
                    confirm_txt_button.text = "Verify"
                }
            }


    }


    override fun onStart() {
        super.onStart()
        confirm_button.isEnabled = true
        confirm_progressbar.visibility = View.GONE
        confirm_txt_button.text = "Verify"
    }


}