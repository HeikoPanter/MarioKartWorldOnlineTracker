package com.rain.mariokartworldonlinetracker

import android.content.Context
import android.os.Process
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class GlobalExceptionHandler(
    private val applicationContext: Context,
    private val defaultHandler: Thread.UncaughtExceptionHandler,
    private val activityToBeLaunched: Class<*> // Die Activity, die nach dem Loggen gestartet werden soll (optional, oft die Haupt-Activity)
) : Thread.UncaughtExceptionHandler {

    companion object {
        private const val TAG = "GlobalExceptionHandler"
    }

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        // 1. Logge die Ausnahme
        Log.e(TAG, "Uncaught exception captured:", exception)

        // Optional: Detailliertere Informationen loggen
        val stackTrace = StringWriter()
        exception.printStackTrace(PrintWriter(stackTrace))
        Log.e(TAG, "Full Stacktrace: $stackTrace")

        // Hier könntest du den Fehler auch an einen externen Dienst senden (Firebase Crashlytics, Sentry, etc.)
        // z.B. FirebaseCrashlytics.getInstance().recordException(exception)


        // 2. Beende die App kontrolliert.
        // Es gibt verschiedene Ansätze, hier ein paar Optionen:

        // Option A: Starte eine "Crash"-Activity, die dem Nutzer eine Nachricht anzeigt
        // und sich dann selbst beendet (oder dem Nutzer erlaubt, die App neu zu starten).
        // Dies ist oft benutzerfreundlicher als ein sofortiger harter Exit.
        /*
        val intent = Intent(applicationContext, activityToBeLaunched).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            // Du könntest hier auch Fehlerinformationen an die Activity übergeben
            // putExtra("error_message", exception.localizedMessage)
            // putExtra("error_stacktrace", stackTrace.toString())
        }
        applicationContext.startActivity(intent)
        Process.killProcess(Process.myPid())
        exitProcess(10) // Wichtig, um sicherzustellen, dass der Prozess beendet wird
        */

        // Option B: Die App sofort beenden (weniger benutzerfreundlich, aber einfach)
        // Rufe zuerst den Standard-Handler auf, damit Android ggf. noch seine Standard-Fehlerberichterstattung machen kann
        // (z.B. den "App ist abgestürzt"-Dialog anzeigen, falls konfiguriert).
        defaultHandler.uncaughtException(thread, exception)
        // Dann den Prozess beenden.
        Process.killProcess(Process.myPid())
        exitProcess(10) // Ein Exit-Code ungleich 0 signalisiert einen Fehler

        // WICHTIG: Vermeide es, hier langwierige Operationen auszuführen,
        // da das System den Prozess bald beenden könnte. Logging ist okay.
    }
}