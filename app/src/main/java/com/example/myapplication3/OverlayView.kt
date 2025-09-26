package com.example.myapplication3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

class OverlayView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var poseResult: PoseLandmarkerResult? = null
    private var inputImageWidth = 1
    private var inputImageHeight = 1
    private var scaleFactor = 1f
    private var offsetX = 0f
    private var offsetY = 0f
    private var mirrorHorizontally = false
    private var lastRunningMode: RunningMode = RunningMode.LIVE_STREAM
    private var previousLandmarks: MutableList<MutableList<FloatArray>>? = null
    private var smoothingAlpha: Float = 0.6f
    private val pointPaint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        style = Paint.Style.FILL
    }
    private val linePaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    fun setResults(result: PoseLandmarkerResult?, imageHeight: Int, imageWidth: Int, runningMode: RunningMode = RunningMode.LIVE_STREAM) {
        poseResult = result
        inputImageHeight = imageHeight
        inputImageWidth = imageWidth
        lastRunningMode = runningMode
        if (width > 0 && height > 0) {
            scaleFactor = when (runningMode) {
                RunningMode.IMAGE, RunningMode.VIDEO -> kotlin.math.min(width * 1f / imageWidth, height * 1f / imageHeight)
                RunningMode.LIVE_STREAM -> kotlin.math.max(width * 1f / imageWidth, height * 1f / imageHeight)
            }
            // fillStart 對齊左上，不需要偏移；若改為 fitCenter，可呼叫 calculateScaleAndOffset()
            offsetX = 0f
            offsetY = 0f
        }
        invalidate()
    }

    fun setMirrorHorizontally(enabled: Boolean) {
        mirrorHorizontally = enabled
        invalidate()
    }

    fun setSmoothing(alpha: Float) {
        smoothingAlpha = alpha.coerceIn(0f, 0.99f)
    }

    private fun calculateScaleAndOffset() {
        if (inputImageWidth <= 0 || inputImageHeight <= 0 || width <= 0 || height <= 0) return
        val viewRatio = width.toFloat() / height
        val imageRatio = inputImageWidth.toFloat() / inputImageHeight
        if (viewRatio > imageRatio) {
            scaleFactor = height.toFloat() / inputImageHeight
            offsetX = (width - inputImageWidth * scaleFactor) / 2f
            offsetY = 0f
        } else {
            scaleFactor = width.toFloat() / inputImageWidth
            offsetX = 0f
            offsetY = (height - inputImageHeight * scaleFactor) / 2f
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (inputImageWidth > 0 && inputImageHeight > 0) {
            scaleFactor = when (lastRunningMode) {
                RunningMode.IMAGE, RunningMode.VIDEO -> kotlin.math.min(w * 1f / inputImageWidth, h * 1f / inputImageHeight)
                RunningMode.LIVE_STREAM -> kotlin.math.max(w * 1f / inputImageWidth, h * 1f / inputImageHeight)
            }
            offsetX = 0f
            offsetY = 0f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
         poseResult?.let { result ->
             // ensure buffer init
             if (previousLandmarks == null || previousLandmarks!!.size != result.landmarks().size) {
                 previousLandmarks = MutableList(result.landmarks().size) { mutableListOf() }
             }
             result.landmarks().forEachIndexed { poseIdx, landmarkList ->
                 val prevForPose = previousLandmarks!![poseIdx]
                 if (prevForPose.size != landmarkList.size) {
                     prevForPose.clear()
                     repeat(landmarkList.size) { prevForPose.add(floatArrayOf(-1f, -1f)) }
                 }

                 // smooth list
                 val smoothed = ArrayList<FloatArray>(landmarkList.size)
                 landmarkList.forEachIndexed { i, lm ->
                     val prev = prevForPose[i]
                     val x = lm.x()
                     val y = lm.y()
                     val sx = if (prev[0] < 0f) x else (smoothingAlpha * prev[0] + (1 - smoothingAlpha) * x)
                     val sy = if (prev[1] < 0f) y else (smoothingAlpha * prev[1] + (1 - smoothingAlpha) * y)
                     prev[0] = sx
                     prev[1] = sy
                     smoothed.add(floatArrayOf(sx, sy))
                 }

                 // draw connections first
                 PoseLandmarker.POSE_LANDMARKS.forEach {
                     val sIdx = it!!.start()
                     val eIdx = it.end()
                     if (sIdx < smoothed.size && eIdx < smoothed.size) {
                         val s = smoothed[sIdx]
                         val e = smoothed[eIdx]
                         canvas.drawLine(
                             s[0] * inputImageWidth * scaleFactor + offsetX,
                             s[1] * inputImageHeight * scaleFactor + offsetY,
                             e[0] * inputImageWidth * scaleFactor + offsetX,
                             e[1] * inputImageHeight * scaleFactor + offsetY,
                             linePaint
                         )
                     }
                 }
                 // draw points on top
                 smoothed.forEach { p ->
                     val cx = p[0] * inputImageWidth * scaleFactor + offsetX
                     val cy = p[1] * inputImageHeight * scaleFactor + offsetY
                     canvas.drawCircle(cx, cy, POINT_RADIUS, pointPaint)
                 }
             }
         }
    }

    companion object {
        // BlazePose 33 landmark skeleton
        val DEFAULT_SKELETON_CONNECTIONS = listOf(
            11 to 12, 11 to 13, 13 to 15, 12 to 14, 14 to 16,
            11 to 23, 12 to 24, 23 to 24, 23 to 25, 25 to 27,
            24 to 26, 26 to 28, 27 to 31, 28 to 32
        )
        private const val POINT_RADIUS = 8f
    }
}
