package com.example.mealorder

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_confirm)
        val tvFinalSummary = findViewById<TextView>(R.id.tvFinalSummary)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)


        val orderSummary = intent.getStringExtra("orderSummary") ?: "訂單資料遺失"


        tvFinalSummary.text = orderSummary


        btnConfirm.setOnClickListener {

            finish()
        }
    }
}