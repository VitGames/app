package io.techmeskills.an02onl_plannerapp

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import io.techmeskills.an02onl_plannerapp.alarmservice.AlarmReceiver
import io.techmeskills.an02onl_plannerapp.cloud.ApiInterface
import io.techmeskills.an02onl_plannerapp.database.DatabaseConstructor
import io.techmeskills.an02onl_plannerapp.database.PlannerDatabase
import io.techmeskills.an02onl_plannerapp.screen.main.*
import io.techmeskills.an02onl_plannerapp.screen.splash.SplashViewModel
import io.techmeskills.an02onl_plannerapp.support.CloudManager
import io.techmeskills.an02onl_plannerapp.support.NotificationRepository
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PlannerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlannerApp)
            modules(listOf(viewModels, storageModule,cloudModule,systemModule))
        }
    }

    private val viewModels = module {
        single { SharedPref(get()) }
        viewModel { NoteDetailsViewModel(get(),get(),get(),get()) }
        viewModel { MainViewModel(get(),get(),get(),get(),get()) }
        viewModel { UsersViewModel(get(),get(),get()) }
    }
    private val storageModule = module {
        single { DatabaseConstructor.create(get()) }  //создаем синглтон базы данных
        factory { get<PlannerDatabase>().notesDao() } //предоставляем доступ для конкретной Dao (в нашем случае NotesDao)
        factory { get<PlannerDatabase>().userDao() }
        factory { NotificationRepository(get(), get()) }
    }
    private val cloudModule = module {
        factory { ApiInterface.get() }
        factory { CloudManager(get(),get(),get(),get()) }
    }
    private val systemModule = module {
        factory { get<Context>().getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    }
}