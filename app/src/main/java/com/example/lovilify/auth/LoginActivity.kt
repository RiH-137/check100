package com.example.lovilify.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.example.lovilify.MainActivity
import com.example.lovilify.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

   // val auth = FirebaseAuth.getInstance()
    private lateinit var auth: FirebaseAuth
  //  private var verificationId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

        binding.sendOtp.setOnClickListener{
            if(binding.userNumber.text!!.isEmpty()){
                Toast.makeText(this, "Please enter your Number!", Toast.LENGTH_SHORT).show()
            }
            else{
                var intent=Intent(this, OtpActivity::class.java)
                intent.putExtra("number", binding.userNumber.text!!)
                startActivity(intent)

            }
        }

    }

}



