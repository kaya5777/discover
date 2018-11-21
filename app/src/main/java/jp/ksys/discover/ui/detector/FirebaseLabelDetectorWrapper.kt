package jp.ksys.discover.ui.detector

import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions


internal class FirebaseLabelDetectorWrapper {

    private val labelDetectorOptions: FirebaseVisionLabelDetectorOptions by lazy {
        FirebaseVisionLabelDetectorOptions.Builder()
                .build()
    }

    private val labelDetector: FirebaseVisionLabelDetector by lazy {
        FirebaseVision.getInstance().getVisionLabelDetector(labelDetectorOptions)
    }

    fun process(image: FirebaseVisionImage,
                onSuccess: (MutableList<FirebaseVisionLabel>) -> Unit,
                onError: (Exception) -> Unit) {
        labelDetector.detectInImage(image)
                .addOnSuccessListener {
                    onSuccess(it)
                }
                .addOnFailureListener {
                    onError(it)
                }
    }
}