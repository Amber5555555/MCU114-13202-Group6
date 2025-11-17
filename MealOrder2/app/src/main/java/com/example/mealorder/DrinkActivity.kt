package com.example.mealorder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class DrinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)

        val rgDrink = findViewById<RadioGroup>(R.id.rgDrink)
        val btnDone = findViewById<Button>(R.id.btnDone)

        btnDone.setOnClickListener {
            val checkedId = rgDrink.checkedRadioButtonId
            val drink = if (checkedId != -1) {
                findViewById<RadioButton>(checkedId).text.toString()
            } else {
                "未選擇"
            }

            val resultIntent = Intent().apply {
                putExtra("drink", drink)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}