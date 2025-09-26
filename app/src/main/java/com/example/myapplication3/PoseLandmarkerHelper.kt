package com.example.myapplication3

import android.content.Context
import androidx.camera.core.ImageProxy
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.google.mediapipe.tasks.core.BaseOptions

class PoseLandmarkerHelper(
    context: Context,
    private val runningMode: RunningMode,
    private val minPoseDetectionConfidence: Float,
    private val minPoseTrackingConfidence: Float,
    private val minPosePresenceConfidence: Float,
    private val currentDelegate: Int,
    private val poseLandmarkerHelperListener: LandmarkerListener
) {

    interface LandmarkerListener {
        fun onResults(resultBundle: ResultBundle)
        fun onError(error: String, errorCode: Int)
    }

    data class ResultBundle(
        val results: List<PoseLandmarkerResult>,
        val inputImageHeight: Int,
        val inputImageWidth: Int,
        val inferenceTime: Long = 0L
    )

    private var poseLandmarker: PoseLandmarker? = null

    init {
        setupPoseLandmarker(context)
    }

    private fun setupPoseLandmarker(context: Context) {
        try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath("pose_landmarker_full.task") // 你放在 assets 的模型
                .build()

            poseLandmarker = PoseLandmarker.createFromOptions(
                context,
                PoseLandmarker.PoseLandmarkerOptions.builder()
                    .setBaseOptions(baseOptions)
                    .setRunningMode(runningMode)
                    .build()
            )
        } catch (e: Exception) {
            poseLandmarkerHelperListener.onError(e.message ?: "Unknown", -1)
        }
    }

    fun detectLiveStream(imageProxy: ImageProxy, isFrontCamera: Boolean) {
        val bitmap = imageProxy.toBitmap() ?: return

        val mpImage = com.google.mediapipe.framework.image.BitmapImageBuilder(bitmap).build()
        val result = poseLandmarker?.detect(mpImage) ?: return

        val bundle = ResultBundle(
            results = listOf(result),
            inputImageHeight = bitmap.height,
            inputImageWidth = bitmap.width
        )

        poseLandmarkerHelperListener.onResults(bundle)

        imageProxy.close()
    }

    fun clearPoseLandmarker() {
        poseLandmarker?.close()
        poseLandmarker = null
    }
}

// extension function to convert ImageProxy to Bitmap
fun ImageProxy.toBitmap(): android.graphics.Bitmap? {
    val planeProxy = this.planes.firstOrNull() ?: return null
    val buffer = planeProxy.buffer
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
