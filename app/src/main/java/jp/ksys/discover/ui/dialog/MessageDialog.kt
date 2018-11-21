package jp.ksys.discover.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import jp.ksys.discover.R

class MessageDialog : DialogFragment() {

    companion object {

        private const val KEY_MESSAGE = "message"

        fun instance(message: String?): MessageDialog =
                MessageDialog().apply {
                    arguments = Bundle().apply {
                        putString(KEY_MESSAGE, message)
                    }
                }
    }

    private val message: String? by lazy {
        val arguments = arguments ?: return@lazy ""
        return@lazy arguments.getString(KEY_MESSAGE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setMessage(getString(R.string.mesasge_discover, message))
                .setPositiveButton(getString(R.string.action_do_ok), null)
                .create()
    }
}