MyApplication3 – Pose Skeleton Detection (CameraX + MediaPipe)

Overview
- Android app that detects human pose in real-time and draws a skeleton overlay.
- Built with CameraX and MediaPipe Tasks Pose Landmarker (LIVE_STREAM).

Key Features
- Live camera preview with front camera.
- Pose landmarks and skeleton overlay with anti-aliased points.
- Optional horizontal mirroring for front camera.
- Temporal smoothing to reduce jitter (exponential moving average).
- Switchable model variants via `MainViewModel` (lite/full/heavy) and thresholds.

Project Structure (relevant)
- app/src/main/java/com/example/myapplication3/
  - RecordActivity.kt: CameraX pipeline, MediaPipe Pose Landmarker setup, feeds results to overlay.
  - OverlayView.kt: Draws smoothed landmarks and skeleton, supports mirroring and scaling.
  - MainViewModel.kt: Centralizes model/delegate/threshold/numPoses settings.
  - (UI) res/layout/activity_record.xml: `PreviewView` + `OverlayView` layered.

Requirements
- Android Studio Flamingo+ recommended, Gradle Wrapper included.
- Android 7.0+ (API 24+) device with camera.
- Assets: place MediaPipe task files in `app/src/main/assets/` (at least one):
  - pose_landmarker_lite.task
  - pose_landmarker_full.task
  - pose_landmarker_heavy.task

Permissions
- CAMERA runtime permission is requested on first launch of `RecordActivity`.

Build & Run
1) Ensure `.task` model files exist under `app/src/main/assets/`.
2) Build and run on a physical device (front camera used by default).
3) From your app flow, start `RecordActivity` to see pose overlay.

How It Works
- CameraX `ImageAnalysis` outputs RGBA frames.
- Frames are rotated/mirrored (front camera) and wrapped into `MPImage`.
- MediaPipe Pose Landmarker runs in LIVE_STREAM via `detectAsync(...)`.
- Results are pushed to `OverlayView` which calculates scale and draws:
  - First the skeleton connections (PoseLandmarker.POSE_LANDMARKS),
  - Then the landmark points on top, using smoothed coordinates.

Tuning Accuracy & Stability
- In `MainViewModel.kt`:
  - `currentModel`: lite/full/heavy (speed vs accuracy).
  - `minPoseDetectionConfidence`, `minPosePresenceConfidence`, `minPoseTrackingConfidence`.
  - `numPoses`: number of persons to detect.
- In `OverlayView.kt`:
  - `setSmoothing(alpha: Float)`: 0.4–0.85 typical. Higher = smoother, slower response.
  - `setMirrorHorizontally(true)`: for front camera.

Preview Scaling
- `PreviewView` uses `app:scaleType="fillStart"` for alignment similar to the official sample.
- If you prefer letter/pillar boxing symmetry, switch to `fitCenter` and adjust overlay scaling accordingly.

Common Issues
- No landmarks: confirm model files in assets and CAMERA permission granted.
- Misaligned overlay: try `fitCenter` and enable `calculateScaleAndOffset()` path, or keep `fillStart`.
- Jitter: increase smoothing (`OverlayView.setSmoothing(0.7f)`) or raise confidence thresholds.

Licensing Note
- MediaPipe models and sample references follow their respective licenses. See Google MediaPipe documentation for details.

Android資料夾內為官方示範程式碼
