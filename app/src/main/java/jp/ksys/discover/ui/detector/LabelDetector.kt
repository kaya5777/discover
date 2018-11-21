package jp.ksys.discover.ui.detector

import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import jp.ksys.discover.ui.detector.model.Frame

class LabelDetector {

    companion object {
        private val TAG = LabelDetector::class.java.simpleName

        private const val RIGHT_ANGLE = 90
    }

    interface DetectListener {
        fun onSuccess(list: MutableList<FirebaseVisionLabel>)

        fun onError()
    }

    var detectListener: DetectListener? = null

    private val firebaseLabelDetectorWrapper = FirebaseLabelDetectorWrapper()

    fun process(frame: Frame) {
        detectLabelsIn(frame)
    }

    private fun detectLabelsIn(frame: Frame) {
        frame.data?.let {
            firebaseLabelDetectorWrapper.process(
                    image = convertFrameToImage(frame),
                    onSuccess = {
                        detectListener?.onSuccess(it)
                    },
                    onError = {
                        detectListener?.onError()
                    })
        }
    }

    private fun convertFrameToImage(frame: Frame) =
            FirebaseVisionImage.fromByteArray(frame.data!!, extractFrameMetadata(frame))

    private fun extractFrameMetadata(frame: Frame): FirebaseVisionImageMetadata =
            FirebaseVisionImageMetadata.Builder()
                    .setWidth(frame.size.width)
                    .setHeight(frame.size.height)
                    .setFormat(frame.format)
                    .setRotation(frame.rotation / RIGHT_ANGLE)
                    .build()
}