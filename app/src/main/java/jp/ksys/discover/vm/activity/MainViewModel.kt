package jp.ksys.discover.vm.activity

import android.databinding.BaseObservable
import android.util.Log
import android.view.View
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import io.reactivex.subjects.PublishSubject
import jp.ksys.discover.ui.detector.LabelDetector
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseObservable() {

    val settingSubject: PublishSubject<Unit> = PublishSubject.create()

    val discoverSubject: PublishSubject<String> = PublishSubject.create()

    val onClickSetting = View.OnClickListener {
        settingSubject.onNext(Unit)
    }

    var target: String? = null

    val detectListener = object : LabelDetector.DetectListener {
        override fun onSuccess(list: MutableList<FirebaseVisionLabel>) {
            val t = target ?: return
            if (list.map { it.label.toLowerCase() }.contains(t)) {
                discoverSubject.onNext(t)
            }
        }

        override fun onError() {
        }
    }
}