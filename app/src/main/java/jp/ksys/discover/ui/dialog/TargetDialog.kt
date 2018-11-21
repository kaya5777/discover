package jp.ksys.discover.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import dagger.android.support.AndroidSupportInjection
import jp.ksys.discover.R
import jp.ksys.discover.databinding.DialogTargetBinding
import jp.ksys.discover.vm.dialog.TargetViewModel
import javax.inject.Inject

class TargetDialog : DialogFragment() {

    companion object {
        val instance = TargetDialog()
    }

    interface DialogListener {
        fun onClickOk(s: String?)
    }

    @Inject
    lateinit var viewModel: TargetViewModel

    var dialogListener: DialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidSupportInjection.inject(this)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding: DialogTargetBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_target, null, false)
        binding.viewModel = viewModel

        return AlertDialog.Builder(activity)
                .setView(binding.root)
                .setPositiveButton(getString(R.string.action_do_ok)) { _, _ -> dialogListener?.onClickOk(viewModel.target) }
                .setNegativeButton(getString(R.string.action_do_cancel), null)
                .create()
    }
}