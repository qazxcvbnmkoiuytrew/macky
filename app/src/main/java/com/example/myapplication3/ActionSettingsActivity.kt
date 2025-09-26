package com.example.myapplication3

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActionSettingsActivity : AppCompatActivity() {

    private val PREFS_NAME = "ActionPrefs"
    private val PREF_SELECTED_ACTIONS = "SelectedActions"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_settings)

        val cbKnee: CheckBox = findViewById(R.id.cbKnee)
        val cbSquat: CheckBox = findViewById(R.id.cbSquat)
        val cbHeelToe: CheckBox = findViewById(R.id.cbHeelToe)
        val cbStep: CheckBox = findViewById(R.id.cbStep)
        val cbToeStand: CheckBox = findViewById(R.id.cbToeStand)
        val buttonSave: Button = findViewById(R.id.buttonSaveActions)

        // 載入已選擇的動作
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val saved = prefs.getStringSet(PREF_SELECTED_ACTIONS, emptySet()) ?: emptySet()
        cbKnee.isChecked = "雙膝交互抬高" in saved
        cbSquat.isChecked = "深蹲" in saved
        cbHeelToe.isChecked = "腳跟對腳尖" in saved
        cbStep.isChecked = "左右跨步" in saved
        cbToeStand.isChecked = "顛腳尖站立" in saved

        buttonSave.setOnClickListener {
            val selected = mutableSetOf<String>()
            if (cbKnee.isChecked) selected.add("雙膝交互抬高")
            if (cbSquat.isChecked) selected.add("深蹲")
            if (cbHeelToe.isChecked) selected.add("腳跟對腳尖")
            if (cbStep.isChecked) selected.add("左右跨步")
            if (cbToeStand.isChecked) selected.add("顛腳尖站立")

            prefs.edit().putStringSet(PREF_SELECTED_ACTIONS, selected).apply()
            Toast.makeText(this, "動作已儲存", Toast.LENGTH_SHORT).show()

            // 儲存後自動返回 SettingsActivity
            finish()
        }
    }
}
