package com.batman.app

import android.app.Application
import com.batman.app.di.DaggerAppComponent
import io.reactivex.plugins.RxJavaPlugins

class App : Application() {

    private fun handleUncaughtExceptionHandler(e: Throwable) {
        try {
            var exceprionMessage = "****************************************************\n"
            exceprionMessage += """
            exception ClassName    : ${e.javaClass.simpleName}
            
            """.trimIndent()
            if (e.stackTrace.size != 0) {
                exceprionMessage += """
                exception MethodName   : ${e.stackTrace[0].methodName}
                
                """.trimIndent()
                exceprionMessage += """
                exception LineNumber   : ${e.stackTrace[0].lineNumber}
                
                """.trimIndent()
            }
            exceprionMessage += """
            exception Message      : ${e.message}
            
            """.trimIndent()
            exceprionMessage += "****************************************************\n"
//            FirebaseCrashlytics.getInstance().recordException(e)
        } catch (ex: java.lang.Exception) {
//            FirebaseCrashlytics.getInstance().recordException(ex)
        }
    }


    override fun onCreate() {
        super.onCreate()
        inject()

        RxJavaPlugins.setErrorHandler { throwable: Throwable? ->
            println("exception check ${throwable?.message}")
            if (throwable != null)
                handleUncaughtExceptionHandler(throwable)
        }
        Thread.setDefaultUncaughtExceptionHandler { _, ex ->
            handleUncaughtExceptionHandler(ex)
        }

    }

    fun inject() {
        DaggerAppComponent.builder().app(this).build()
    }

}