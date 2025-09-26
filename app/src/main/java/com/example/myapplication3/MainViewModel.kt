package com.example.myapplication3

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    // Delegate: 0 CPU, 1 GPU (GPU requires proper setup)
    var currentDelegate: Int = DELEGATE_CPU
        private set

    // Model variant: 0 full, 1 lite, 2 heavy
    var currentModel: Int = MODEL_POSE_LANDMARKER_FULL
        private set

    var minPoseDetectionConfidence: Float = DEFAULT_POSE_DETECTION_CONFIDENCE
        private set

    var minPoseTrackingConfidence: Float = DEFAULT_POSE_TRACKING_CONFIDENCE
        private set

    var minPosePresenceConfidence: Float = DEFAULT_POSE_PRESENCE_CONFIDENCE
        private set

    var numPoses: Int = DEFAULT_NUM_POSES
        private set

    fun setDelegate(delegate: Int) { currentDelegate = delegate }
    fun setModel(model: Int) { currentModel = model }
    fun setMinPoseDetectionConfidence(value: Float) { minPoseDetectionConfidence = value }
    fun setMinPoseTrackingConfidence(value: Float) { minPoseTrackingConfidence = value }
    fun setMinPosePresenceConfidence(value: Float) { minPosePresenceConfidence = value }
    fun setNumPoses(value: Int) { numPoses = value }

    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1

        const val MODEL_POSE_LANDMARKER_FULL = 0
        const val MODEL_POSE_LANDMARKER_LITE = 1
        const val MODEL_POSE_LANDMARKER_HEAVY = 2

        const val DEFAULT_POSE_DETECTION_CONFIDENCE = 0.5f
        const val DEFAULT_POSE_TRACKING_CONFIDENCE = 0.5f
        const val DEFAULT_POSE_PRESENCE_CONFIDENCE = 0.5f
        const val DEFAULT_NUM_POSES = 1
    }
}


