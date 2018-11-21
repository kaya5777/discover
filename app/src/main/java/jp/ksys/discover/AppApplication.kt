package jp.ksys.discover

import dagger.android.support.DaggerApplication
import jp.ksys.discover.di.component.AppComponent
import jp.ksys.discover.di.component.DaggerAppComponent
import jp.ksys.discover.di.modules.AppModule

class AppApplication : DaggerApplication() {

    override fun applicationInjector(): AppComponent {
        return DaggerAppComponent.builder()
                .application(this)
                .appModule(AppModule(this))
                .build()
    }
}