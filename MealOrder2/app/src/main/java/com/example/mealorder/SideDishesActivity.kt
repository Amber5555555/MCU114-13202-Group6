package com.example.mealorder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SideDishesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_dishes)

        val cbFries = findViewById<CheckBox>(R.id.cbFries)
        val cbSalad = findViewById<CheckBox>(R.id.cbSalad)
        val cbCornCup = findViewById<CheckBox>(R.id.cbCornCup)
        val btnDone = findViewById<Button>(R.id.btnDone)

        btnDone.setOnClickListener {
            val selectedDishes = mutableListOf<String>()
            if (cbFries.isChecked) selectedDishes.add(cbFries.text.toString())
            if (cbSalad.isChecked) selectedDishes.add(cbSalad.text.toString())
            if (cbCornCup.isChecked) selectedDishes.add(cbCornCup.text.toString())

            if (selectedDishes.isEmpty()) {

                Toast.makeText(this, "Please choose at least one side dish", Toast.LENGTH_SHORT).show()
            } else {
                val sideDishesText = selectedDishes.joinToString(", ") // 用逗號分隔
                val resultIntent = Intent().apply {
                    putExtra("sideDishes", sideDishesText)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}