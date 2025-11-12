package com.example.mealorder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class MainMealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_meal)

        val rgMainMeal = findViewById<RadioGroup>(R.id.rgMainMeal)
        val btnDone = findViewById<Button>(R.id.btnDone)

        btnDone.setOnClickListener {
            val checkedId = rgMainMeal.checkedRadioButtonId
            val mainMeal = if (checkedId != -1) {
                val selectedRadioButton = rgMainMeal.findViewById<RadioButton>(checkedId)
                selectedRadioButton.text.toString()
            } else {
                "未選擇"
            }

            val resultIntent = Intent().apply {
                putExtra("mainMeal", mainMeal)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}