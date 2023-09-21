package com.example.lovilify.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.lovilify.R
import com.example.lovilify.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOtpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityOtpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)

        builder.setMessage("Please wait...")
        builder.setTitle("loading")
        builder.setCancelable(false)

//        dialog = builder.create()
//        dialog.show()

        //abhi
        dialog=AlertDialog.Builder(this).setView(R.layout.loding_layout).create()

        val phoneNumber ="+91"+intent.getStringExtra("number")

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Replace 'phoneNumber' with the actual phone number to verify
            .setTimeout(210L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // This callback is invoked when the verification is successful
                    // You can handle the success case here, e.g., sign in the user
                    Toast.makeText(this@OtpActivity, "Verification Successful", Toast.LENGTH_SHORT).show()
                   

                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    // This callback is invoked when the verification fails
                    dialog.dismiss()
                    Toast.makeText(this@OtpActivity, "Verification failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, token)

                }
            }).build()

        //for sending OTP
        PhoneAuthProvider.verifyPhoneNumber(options)


        // enrtering otp by the user
        binding.verifyOtp.setOnClickListener{
            if(binding.userOtp.text!!.isEmpty()){
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()
            }
            else{
                dialog.show()
                val credential = PhoneAuthProvider.getCredential(verificationId, binding.userOtp.text!!.toString())

                auth.signInWithCredential(credential)
                    .addOnCompleteListener{
                        if(it.isSuccessful){



                            dialog.dismiss()

                            val intent= Intent(this@OtpActivity, RegisterActivity::class.java)
                            startActivity(intent)
                            finish()

                        }
                        else{
                            dialog.dismiss()
                            Toast.makeText(this, "Error ${it.exception}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
    }
}