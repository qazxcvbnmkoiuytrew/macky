package com.example.myapplication3

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class IndividualActionsActivity : AppCompatActivity() {

    private val PREFS_NAME = "ActionPrefs"
    private val PREF_SELECTED_ACTIONS = "SelectedActions"

    private lateinit var buttonContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_actions)

        buttonContainer = findViewById(R.id.individualButtonContainer)

        // 讀取動作設定頁儲存的選擇
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val selectedActions = prefs.getStringSet(PREF_SELECTED_ACTIONS, emptySet()) ?: emptySet()

        if (selectedActions.isEmpty()) {
            Toast.makeText(this, "請先在動作設定選擇動作", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 動態建立按鈕
        selectedActions.forEach { action ->
            val btn = Button(this).apply {
                text = action
                setOnClickListener {
                    Toast.makeText(this@IndividualActionsActivity, "點擊：$action", Toast.LENGTH_SHORT).show()
                    // 這裡可以做個別動作示範或跳轉
                }
            }
            buttonContainer.addView(btn)
        }
    }
}
