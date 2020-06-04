package com.himanshu.whatsappclone.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.himanshu.whatsappclone.LoginActivity
import com.himanshu.whatsappclone.R

class MainPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        findViewById<Button>(R.id.button_logout).setOnClickListener {
            run {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
                return@run
            }
        }

        findViewById<Button>(R.id.button_find_user).setOnClickListener{
            run {
                val intent = Intent (applicationContext, FindUserActivity::class.java)
                startActivity (intent)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPermissions() {
        requestPermissions (arrayOf (Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS), 1)
    }
}
