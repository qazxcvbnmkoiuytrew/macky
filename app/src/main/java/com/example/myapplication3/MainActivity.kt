package com.example.myapplication3

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonStart: Button = findViewById(R.id.buttonStart)
        val buttonIndividual: Button = findViewById(R.id.buttonIndividual)
        val buttonSettings: Button = findViewById(R.id.buttonSettings)
        val buttonHistory: Button = findViewById(R.id.buttonHistory)

        buttonStart.setOnClickListener {
            Toast.makeText(this, "點擊 一件開始", Toast.LENGTH_SHORT).show()
        }

        buttonIndividual.setOnClickListener {
            Toast.makeText(this, "點擊 個別動作", Toast.LENGTH_SHORT).show()
        }

        buttonSettings.setOnClickListener {
            Toast.makeText(this, "點擊 設定", Toast.LENGTH_SHORT).show()
        }

        buttonHistory.setOnClickListener {
            Toast.makeText(this, "點擊 歷史", Toast.LENGTH_SHORT).show()
        }

        // 「設定」
        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // 「一鍵開始」
        buttonStart.setOnClickListener {
            val intent = Intent(this, DemoActivity::class.java)
            startActivity(intent)
        }

        // 「個別動作」
        buttonIndividual.setOnClickListener {
            val intent = Intent(this, IndividualActionsActivity::class.java)
            startActivity(intent)
        }
    }
}
