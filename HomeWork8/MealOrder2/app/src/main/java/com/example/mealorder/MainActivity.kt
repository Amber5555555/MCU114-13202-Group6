package com.example.mealorder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvOrderSummary: TextView
    private var mainMeal: String = "未選擇"
    private var sideDishes: String = "未選擇"
    private var drink: String = "未選擇"


    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data

            intent?.getStringExtra("mainMeal")?.let { mainMeal = it }
            intent?.getStringExtra("sideDishes")?.let { sideDishes = it }
            intent?.getStringExtra("drink")?.let { drink = it }
            updateOrderSummary()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tvOrderSummary = findViewById(R.id.tvOrderSummary)
        val btnGoToMainMeal = findViewById<Button>(R.id.btnGoToMainMeal)
        val btnGoToSideDishes = findViewById<Button>(R.id.btnGoToSideDishes)
        val btnGoToDrink = findViewById<Button>(R.id.btnGoToDrink)


        val btnConfirmOrder = findViewById<Button>(R.id.btnConfirmOrder)

        updateOrderSummary()


        btnGoToMainMeal.setOnClickListener {
            val intent = Intent(this, MainMealActivity::class.java)
            startForResult.launch(intent)
        }

        btnGoToSideDishes.setOnClickListener {
            val intent = Intent(this, SideDishesActivity::class.java)
            startForResult.launch(intent)
        }

        btnGoToDrink.setOnClickListener {
            val intent = Intent(this, DrinkActivity::class.java)
            startForResult.launch(intent)
        }




        btnConfirmOrder.setOnClickListener {

            val intent = Intent(this, ConfirmActivity::class.java)


            val orderSummaryText = tvOrderSummary.text.toString()


            intent.putExtra("orderSummary", orderSummaryText)


            startActivity(intent)
        }
    }



    private fun updateOrderSummary() {
        tvOrderSummary.text = "主餐：$mainMeal\n\n副餐：$sideDishes\n\n飲料：$drink"
    }
}