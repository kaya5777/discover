package jp.ksys.discover.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.ksys.discover.ui.activity.MainActivity
import jp.ksys.discover.ui.dialog.TargetDialog

@Module
abstract class ViewModule {

    @ContributesAndroidInjector()
    internal abstract fun contributeMainActivity(): MainActivity


    @ContributesAndroidInjector()
    internal abstract fun contributeTargetDialog(): TargetDialog

}