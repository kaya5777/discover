package jp.ksys.discover.ui.activity

import android.content.Context
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.Facing
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import jp.ksys.discover.R
import jp.ksys.discover.databinding.ActivityMainBinding
import jp.ksys.discover.ui.detector.LabelDetector
import jp.ksys.discover.ui.detector.model.Frame
import jp.ksys.discover.ui.detector.model.Size
import jp.ksys.discover.ui.dialog.MessageDialog
import jp.ksys.discover.ui.dialog.TargetDialog
import jp.ksys.discover.vm.activity.MainViewModel
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {

        private const val PERMISSION_REQUESTS = 1

        private fun isPermissionGranted(context: Context, permission: String): Boolean {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            return false
        }
    }

    @Inject
    lateinit var viewModel: MainViewModel

    private val disposables: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private val requiredPermissions: Array<String?>
        get() {
            return try {
                val info = this.packageManager
                        .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
                val ps = info.requestedPermissions
                if (ps != null && ps.isNotEmpty()) {
                    ps
                } else {
                    arrayOfNulls(0)
                }
            } catch (e: Exception) {
                arrayOfNulls(0)
            }
        }

    private var cameraView: CameraView? = null

    private val dialogListener = object : TargetDialog.DialogListener {
        override fun onClickOk(s: String?) {
            viewModel.target = s
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        viewModel.settingSubject.subscribe {
            val dialog = TargetDialog.instance
            dialog.dialogListener = dialogListener
            supportFragmentManager.beginTransaction()
                    .add(dialog, null)
                    .commitAllowingStateLoss()
        }.apply { disposables.add(this) }

        viewModel.discoverSubject.subscribe {
            supportFragmentManager.beginTransaction()
                    .add(MessageDialog.instance(it), null)
                    .commitAllowingStateLoss()
        }.apply { disposables.add(this) }

        cameraView = binding.cameraView
        setupCamera()

        if (allPermissionsGranted()) {
            cameraView?.start()
        } else {
            getRuntimePermissions()
        }
    }

    public override fun onResume() {
        super.onResume()
        cameraView?.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView?.stop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
        cameraView?.destroy()
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val allNeededPermissions = ArrayList<String>()
        for (permission in requiredPermissions) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    allNeededPermissions.add(it)
                }
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS)
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (allPermissionsGranted()) {
            cameraView?.start()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setupCamera() {
        cameraView?.addFrameProcessor {
            val labelDetector = LabelDetector()
            labelDetector.detectListener = viewModel.detectListener

            labelDetector.process(Frame(
                    data = it.data,
                    rotation = it.rotation,
                    size = Size(it.size.width, it.size.height),
                    format = it.format,
                    isCameraFacingBack = cameraView?.facing == Facing.BACK))
        }
    }
}
