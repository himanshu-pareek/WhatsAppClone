package com.himanshu.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.himanshu.whatsappclone.activities.MainPageActivity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var mPhoneNumberEditText: EditText? = null
    private var mVerificationCodeEditText: EditText? = null
    private var mSendButton: Button? = null

    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var mVerificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        userIsLoggedIn()

        mPhoneNumberEditText = findViewById(R.id.edit_text_phone_number)
        mVerificationCodeEditText = findViewById(R.id.edit_text_verification_code)
        mSendButton = findViewById(R.id.button_send)

        mSendButton!!.setOnClickListener {
            if (mVerificationId != null) {
                verifyPhoneNumberWithCode()
            } else {
                startPhoneNumberVerification()
            }
        }

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                e.printStackTrace()
                Log.e("MainActivity", e.toString())
            }

            override fun onCodeSent(
                verificationId: String,
                forcedResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, forcedResendingToken)

                mVerificationId = verificationId
                mSendButton!!.text = getString(R.string.verify_code_string)
            }

        }
    }

    private fun verifyPhoneNumberWithCode() {
        val credential: PhoneAuthCredential =
            PhoneAuthProvider.getCredential(mVerificationId!!, mSendButton?.text.toString())
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(phoneAuthCredential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(
            this
        ) { task ->
            run {
                if (task.isSuccessful) {
                    userIsLoggedIn()
                }
            }
        }
    }

    private fun userIsLoggedIn() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val intent = Intent(applicationContext, MainPageActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
    }

    private fun startPhoneNumberVerification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mPhoneNumberEditText?.text.toString(),
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }
}
