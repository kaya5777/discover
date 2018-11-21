package jp.ksys.discover.vm.dialog

import android.databinding.BaseObservable
import android.databinding.Bindable
import javax.inject.Inject

class TargetViewModel @Inject constructor(): BaseObservable() {

    @Bindable
    var target: String? = null
}