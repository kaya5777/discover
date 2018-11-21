package jp.ksys.discover.ui.detector.model

data class Frame(
        val data: ByteArray?,
        val rotation: Int,
        val size: Size,
        val format: Int,
        val isCameraFacingBack: Boolean)

data class Size(val width: Int, val height: Int)