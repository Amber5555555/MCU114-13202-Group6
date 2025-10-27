package com.example.app



import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // 使用 lateinit，延遲初始化變數。
    // 這告訴編譯器：我保證會在第一次使用它之前完成初始化 (在 onCreate 中)。
    // 這樣就不用將它們宣告為可空類型 (TextView?)，也避免了使用 `!!`。
    private lateinit var txtShow: TextView
    private lateinit var btnZero: Button
    private lateinit var btnOne: Button
    private lateinit var btnTwo: Button
    private lateinit var btnThree: Button
    private lateinit var btnFour: Button
    private lateinit var btnFive: Button
    private lateinit var btnSix: Button
    private lateinit var btnSeven: Button
    private lateinit var btnEight: Button
    private lateinit var btnNine: Button
    private lateinit var btnClear: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化所有 UI 元件
        // 在 Kotlin 中，findViewById 會自動推斷類型，不需要 `as TextView`
        txtShow = findViewById(R.id.txtShow)
        btnZero = findViewById(R.id.btnZero)
        btnOne = findViewById(R.id.btnOne)
        btnTwo = findViewById(R.id.btnTwo)
        btnThree = findViewById(R.id.btnThree)
        btnFour = findViewById(R.id.btnFour)
        btnFive = findViewById(R.id.btnFive)
        btnSix = findViewById(R.id.btnSix)
        btnSeven = findViewById(R.id.btnSeven)
        btnEight = findViewById(R.id.btnEight)
        btnNine = findViewById(R.id.btnNine)
        btnClear = findViewById(R.id.btnClear)

        // 建立一個包含所有數字按鈕的列表，方便設定監聽器
        val numberButtons = listOf(
            btnZero, btnOne, btnTwo, btnThree, btnFour,
            btnFive, btnSix, btnSeven, btnEight, btnNine
        )

        // 使用 forEach 迴圈為每個數字按鈕設定點擊監聽器
        numberButtons.forEach { button ->
            button.setOnClickListener {
                // `it` 在這裡代表被點擊的 button
                // 直接使用屬性 `text`
                val currentText = txtShow.text.toString()
                val buttonText = (it as Button).text.toString()
                txtShow.text = currentText + buttonText
            }
        }

        // 單獨為清除按鈕設定監聽器
        btnClear.setOnClickListener {
            txtShow.text = "電話號碼："
        }
    }
}
