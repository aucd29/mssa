package com.example.dhk.di.module

import brigitte.di.dagger.module.RxModule
import brigitte.di.dagger.module.ViewModelFactoryModule
import dagger.Module

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 5. <p/>
 */

@Module(includes = [
    RxModule::class,
    DbModule::class,
    ViewModelModule::class,
    ViewModelFactoryModule::class
])
class UtilModule