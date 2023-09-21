package com.example.lovilify.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.lovilify.MainActivity
import com.example.lovilify.R
import com.example.lovilify.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

@Suppress("UNREACHABLE_CODE")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

   // val auth = FirebaseAuth.getInstance()
    private lateinit var auth: FirebaseAuth
  //  private var verificationId: String? = null

    //abhi
    private lateinit var dialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        //abhi
        dialog=AlertDialog.Builder(this).setView(R.layout.loding_layout).create()

        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

        binding.sendOtp.setOnClickListener{
            dialog.show()
            if(binding.userNumber.text!!.isEmpty()){
                Toast.makeText(this, "Please enter your Number!", Toast.LENGTH_SHORT).show()
            }
            else{
                var intent=Intent(this, OtpActivity::class.java)
                intent.putExtra("number", binding.userNumber.text!!.toString())
                startActivity(intent)


            }

        }
        
   
    }
    //abhi
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){task->
                if (task.isSuccessful){
                    checkUserExist(binding.userNumber.text.toString())

                    
                }
                else{
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }

            }


    }

    private fun checkUserExist(number: String) {

        FirebaseDatabase.getInstance().getReference("users").child(number)
            .addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity, p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    TODO("Not yet implemented")
                    if(p0.exists()){
                        dialog.dismiss()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }else{

                        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                        finish()
                    }
                }
            })

    }

}



