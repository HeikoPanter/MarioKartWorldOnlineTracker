package com.rain.mariokartworldonlinetracker

import android.content.Context

object MkwotSettings {

    private const val PREFERENCES_FILE_KEY = "com.rain.mariokartworldonlinetracker.APP_PREFERENCES"
    private const val KEY_ITEMS_PER_ROW = "items_per_row"
    private const val DEFAULT_ITEMS_PER_ROW = 3

    // Variable, die den aktuellen Wert hält und aus SharedPreferences geladen wird
    var itemsPerRow: Int = DEFAULT_ITEMS_PER_ROW
        private set // Getter ist öffentlich, Setter ist privat, um Änderung nur über save zu erzwingen

    // Funktion zum Initialisieren/Laden der Einstellungen
    // Muss einmal beim App-Start aufgerufen werden, z.B. in Ihrer Application-Klasse oder Haupt-Activity
    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        itemsPerRow = prefs.getInt(KEY_ITEMS_PER_ROW, DEFAULT_ITEMS_PER_ROW)
    }

    // Funktion zum Speichern der Einstellung
    fun saveItemsPerRow(context: Context, value: Int) {
        val prefs = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putInt(KEY_ITEMS_PER_ROW, value)
            apply() // Asynchron speichern
        }
        itemsPerRow = value // Internen Wert auch aktualisieren
        // Optional: Hier einen Mechanismus implementieren, um UI-Komponenten zu benachrichtigen,
        // dass sich die Einstellung geändert hat (z.B. über LiveData, EventBus, oder einen Callback).
    }
}