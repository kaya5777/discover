package jp.ksys.discover.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import jp.ksys.discover.R
import javax.inject.Singleton

@Module(includes = [(ViewModule::class)])
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = this.application

    @Provides
    @Singleton
    fun provideContext(): Context {
        this.application.setTheme(R.style.AppTheme)
        return this.application
    }

}
