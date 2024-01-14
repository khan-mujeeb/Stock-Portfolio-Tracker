package com.example.stockportfoliotracker.authentication


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.example.stockportfoliotracker.activity.MainActivity
import com.example.stockportfoliotracker.data.User
import com.example.stockportfoliotracker.databinding.ActivityOtpactivityBinding
import com.example.stockportfoliotracker.repository.FirebaseRepository
import com.example.stockportfoliotracker.utils.DialogUtils
import com.example.stockportfoliotracker.utils.FirebaseUtils
import com.example.stockportfoliotracker.viewmodel.FirebaseViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var dialog: AlertDialog
    private lateinit var verificationId: String
    private lateinit var builder: AlertDialog.Builder
    private lateinit var viewModel: FirebaseViewModel
    private var phonenumber: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        varibaleInit()
        subscribeUi()

        // verification
        val options = getPhoneAuthOptions(phonenumber)
        PhoneAuthProvider.verifyPhoneNumber(options)

        subscribeOnClick()
    }

    private fun subscribeUi() {
        dialog.show()
    }

    private fun subscribeOnClick() {
        binding.btnContinue.setOnClickListener {
            val otp = binding.etNumber.text.toString()
            if(otp.isNotEmpty()) {
                dialog.show()

                // phone number verification
                val credential = PhoneAuthProvider.getCredential(verificationId,otp)
                FirebaseUtils.firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        dialog.dismiss()
                        val database = FirebaseUtils.firebaseDatabase
                        val uid = it.result.user!!.uid

                        // Check if the phone number already exists in the "users" node
                        val usersRef = database.reference.child("users").child(phonenumber)

                        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    startActivity(Intent(this@OTPActivity, MainActivity::class.java))

                                } else {

                                    val user = User(
                                        phoneNumber = phonenumber,
                                        "0",
                                        "0",
                                        "0"
                                    )

                                    viewModel.addUser(user, phonenumber, this@OTPActivity, MainActivity())
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle errors
                                Toast.makeText(
                                    this@OTPActivity,
                                    "Error: ${databaseError.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                    } else {
                        dialog.dismiss()
                        Toast.makeText(this@OTPActivity, "${it.exception}", Toast.LENGTH_LONG).show()
                    }
                }


            }else {
                Toast.makeText(this@OTPActivity,"Enter OTP", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getPhoneAuthOptions(phonenumber: String):  PhoneAuthOptions {
        return PhoneAuthOptions.newBuilder(FirebaseUtils.firebaseAuth)
            .setPhoneNumber(phonenumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@OTPActivity,"Verification Failed $p0", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    dialog.dismiss()
                    verificationId = p0
                }

            }).build()
    }
    private fun varibaleInit() {
        viewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]
        DialogUtils.buildLoadingDialog(this)
        phonenumber = "+91" + intent.getStringExtra("number").toString()

    }

}