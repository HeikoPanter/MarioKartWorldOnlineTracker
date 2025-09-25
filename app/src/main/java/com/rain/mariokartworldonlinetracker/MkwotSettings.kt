package com.rain.mariokartworldonlinetracker

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MkwotSettings {

    private const val PREFERENCES_FILE_KEY = "com.rain.mariokartworldonlinetracker.APP_PREFERENCES"
    private const val KEY_ITEMS_PER_ROW = "items_per_row"
    private const val KEY_SHOW_ALL_ENTRIES = "show_all_entries"
    private const val KEY_HIDE_100CC = "hide_100cc"
    private const val KEY_AUTO_SELECT_150CC = "auto_select_150cc"
    private const val DEFAULT_ITEMS_PER_ROW = 3
    private const val DEFAULT_SHOW_ALL_ENTRIES = false
    private const val DEFAULT_HIDE_100CC = false
    private const val DEFAULT_AUTO_SELECT_150CC = false

    // Variable, die den aktuellen Wert hält und aus SharedPreferences geladen wird
    var itemsPerRow: Int = DEFAULT_ITEMS_PER_ROW
        private set // Getter ist öffentlich, Setter ist privat, um Änderung nur über save zu erzwingen

    private val _showAllEntriesFlow = MutableStateFlow(DEFAULT_SHOW_ALL_ENTRIES)
    var showAllEntries: StateFlow<Boolean> = _showAllEntriesFlow.asStateFlow()

    var hide100cc: Boolean = false
        private set

    var autoSelect150cc: Boolean = false
        private set

    // Funktion zum Initialisieren/Laden der Einstellungen
    // Muss einmal beim App-Start aufgerufen werden, z.B. in Ihrer Application-Klasse oder Haupt-Activity
    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        itemsPerRow = prefs.getInt(KEY_ITEMS_PER_ROW, DEFAULT_ITEMS_PER_ROW)
        _showAllEntriesFlow.value = prefs.getBoolean(KEY_SHOW_ALL_ENTRIES, DEFAULT_SHOW_ALL_ENTRIES)
        hide100cc = prefs.getBoolean(KEY_HIDE_100CC, DEFAULT_HIDE_100CC)
        autoSelect150cc = prefs.getBoolean(KEY_AUTO_SELECT_150CC, DEFAULT_AUTO_SELECT_150CC)
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

    fun saveShowAllEntries(context: Context, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean(KEY_SHOW_ALL_ENTRIES, value)
            apply() // Asynchron speichern
        }
        _showAllEntriesFlow.value = value
    }

    fun saveHide100cc(context: Context, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean(KEY_HIDE_100CC, value)
            apply() // Asynchron speichern
        }
        hide100cc = value
        if (!hide100cc) {
            saveAutoSelect150cc(context, false)
        }
    }

    fun saveAutoSelect150cc(context: Context, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean(KEY_AUTO_SELECT_150CC, value)
            apply() // Asynchron speichern
        }
        autoSelect150cc = value
    }
}