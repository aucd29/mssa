package com.example.mssa.di.component

import com.example.mssa.MainApp
import com.example.mssa.di.module.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 5. <p/>
 *
 * https://medium.com/@iammert/new-android-injector-with-dagger-2-part-1-8baa60152abe
 * https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample/app/src/main/java/com/android/example/github
 */

// androidx 옵션을 주면 java 파일은 support 를 바라보고 있지만
// 내부적으로는 androidx 로 class 를 생성해서 이를 반영한다.
// 며칠 삽질을 했다..=_ =

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityModule::class,
    ViewModelAssistedFactoriesModule::class,
    UtilModule::class,
    NetworkModule::class
])
interface AppComponent: AndroidInjector<MainApp> {
    @Component.Factory
    interface Factory : AndroidInjector.Factory<MainApp>
}