package com.example.myapplication3

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.VideoView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent


class DemoActivity : AppCompatActivity() {

    private val PREFS_NAME = "ActionPrefs"
    private val PREF_SELECTED_ACTIONS = "SelectedActions"

    private lateinit var videoView: VideoView
    private lateinit var buttonContainer: LinearLayout
    private lateinit var startButton: Button

    // 動作與影片資源對應，影片請放在 res/raw/
    private val actionVideos = mapOf(
        "雙膝交互抬高" to R.raw.knee_lift,
        "深蹲" to R.raw.squat,
        "腳跟對腳尖" to R.raw.heel_toe,
        "左右跨步" to R.raw.side_step,
        "顛腳尖站立" to R.raw.toe_stand
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        videoView = findViewById(R.id.videoView)
        buttonContainer = findViewById(R.id.buttonContainer)
        startButton = findViewById(R.id.buttonStartDemo)

        // 載入使用者選的動作
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val selectedActions = prefs.getStringSet(PREF_SELECTED_ACTIONS, emptySet()) ?: emptySet()

        if (selectedActions.isEmpty()) {
            Toast.makeText(this, "請先在動作設定選擇動作", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 依照選擇建立按鈕切換影片
        selectedActions.forEach { action ->
            val btn = Button(this).apply {
                text = action
                setOnClickListener { playVideoForAction(action) }
            }
            buttonContainer.addView(btn)
        }

        // 預設播放第一個動作影片
        playVideoForAction(selectedActions.first())

        startButton.setOnClickListener {
            // 進入錄影頁面
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playVideoForAction(action: String) {
        val resId = actionVideos[action] ?: return
        val uri = Uri.parse("android.resource://${packageName}/$resId")
        videoView.setVideoURI(uri)
        videoView.start()
    }
}
