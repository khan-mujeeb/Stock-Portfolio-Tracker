package com.example.stockportfoliotracker.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stockportfoliotracker.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.getOtp.setOnClickListener {
            if (checkEditText()) {
                val intent = Intent(this, OTPActivity::class.java)
                intent.putExtra("number", getNumber())
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "enter correct number", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkEditText(): Boolean {
        return binding!!.one.text.toString().isNotEmpty() && binding!!.one.text.toString().length == 10
    }

    // getter to get number
    private fun getNumber(): String {
        return binding!!.one.text.toString()
    }

}






