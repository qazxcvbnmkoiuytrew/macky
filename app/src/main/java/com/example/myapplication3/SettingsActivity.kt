package com.example.myapplication3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonAccountSettings: Button = findViewById(R.id.buttonAccountSettings)
        val buttonActionSettings: Button = findViewById(R.id.buttonActionSettings)
        val buttonDataSync: Button = findViewById(R.id.buttonDataSync)
        val buttonBackHome: Button = findViewById(R.id.buttonBackHome)

        buttonAccountSettings.setOnClickListener {
            // 預留功能
        }

        buttonActionSettings.setOnClickListener {
            val intent = Intent(this, ActionSettingsActivity::class.java)
            startActivity(intent)
        }

        buttonDataSync.setOnClickListener {
            // 預留功能
        }

        // 回主頁
        buttonBackHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}

