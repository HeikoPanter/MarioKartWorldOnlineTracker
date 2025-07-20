package com.rain.mariokartworldonlinetracker

import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rain.mariokartworldonlinetracker.data.RaceResultRepository
import com.rain.mariokartworldonlinetracker.data.pojo.ResultHistory
import com.rain.mariokartworldonlinetracker.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Date
import java.util.Locale
import kotlin.text.format
import kotlin.text.isEmpty

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val raceResultRepository: RaceResultRepository by lazy {
        RaceResultRepository((application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao())
    }

    private lateinit var createFileLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navView.setItemIconTintList(null); // So that the icons appear in their original color
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_start_session,
                R.id.nav_statistics_race_worldwide,
                R.id.nav_statistics_race_versus,
                R.id.nav_statistics_knockout_worldwide,
                R.id.nav_statistics_knockout_versus
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Initialisieren des ActivityResultLauncher für das Erstellen von Dateien
        createFileLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri: Uri? ->
            uri?.let {
                lifecycleScope.launch { // Starten Sie eine Coroutine zum Schreiben der Datei
                    exportDataToUri(it)
                }
            } ?: run {
                Toast.makeText(this, getString(R.string.export_cancelled), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings_items_per_row -> {
                showItemsPerRowDialog()
                true
            }
            R.id.action_settings_show_all_entries -> {
                showShowAllEntriesDialog()
                true
            }
            R.id.action_settings_export_to_csv -> {
                promptToCreateCsvFile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // In Ihrer MainActivity.kt (oder wo immer der Settings-Button ist)
    private fun showItemsPerRowDialog() {
        val options = arrayOf("2", "3", "4", "5") // Mögliche Werte
        val currentSelectionIndex = options.indexOf(MkwotSettings.itemsPerRow.toString())

        AlertDialog.Builder(this)
            .setTitle(R.string.action_settings_items_per_row)
            .setSingleChoiceItems(options, currentSelectionIndex) { dialog, which ->
                // Dieser Block wird sofort ausgeführt, wenn eine Auswahl getroffen wird.
                // Besser ist es, die Auswahl erst beim Klick auf "OK" zu speichern.
            }
            .setPositiveButton(R.string.dialog_ok) { dialog, _ ->
                val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                if (selectedPosition != -1) {
                    val selectedValue = options[selectedPosition].toIntOrNull()
                    selectedValue?.let {
                        MkwotSettings.saveItemsPerRow(applicationContext, it)
                        Toast.makeText(this, "Changed items per row to $it", Toast.LENGTH_SHORT).show()
                        recreate()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.dialog_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showShowAllEntriesDialog() {
        val options = arrayOf(getString(R.string.settings_option_yes), getString(R.string.settings_option_no))
        // Alternativ: val options = arrayOf("An", "Aus") oder "Anzeigen", "Verbergen"

        // Holen Sie den aktuellen Wert aus Ihren Settings (MkwotSettings)
        val currentShowAllEntries = MkwotSettings.showAllEntries
        val currentSelectionIndex = if (currentShowAllEntries.value) 0 else 1 // 0 für "Ja/An", 1 für "Nein/Aus"

        AlertDialog.Builder(this)
            .setTitle(R.string.action_settings_show_all_entries) // Stellen Sie sicher, dass dieser String existiert
            .setSingleChoiceItems(options, currentSelectionIndex) { dialog, which ->
                // Die Auswahl wird erst bei Klick auf "OK" gespeichert.
                // 'which' gibt den Index der ausgewählten Option an.
            }
            .setPositiveButton(R.string.dialog_ok) { dialog, _ ->
                val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                if (selectedPosition != -1) {
                    val selectedValueBoolean = (selectedPosition == 0) // True, wenn "Ja/An" (Index 0) ausgewählt wurde

                    MkwotSettings.saveShowAllEntries(applicationContext, selectedValueBoolean)

                    Toast.makeText(
                        this,
                        "Changed show all entries to $selectedValueBoolean",
                        Toast.LENGTH_SHORT
                    ).show()

                    recreate() // Überdenken Sie dies für eine gezieltere Aktualisierung
                }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.dialog_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun promptToCreateCsvFile() {
        val sdf =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateTime = sdf.format(java.util.Date())
        val fileName = "mkwot_race_history_$currentDateTime.csv"
        createFileLauncher.launch(fileName)
    }

    private suspend fun exportDataToUri(uri: Uri) {
        try {
            val raceResults = raceResultRepository.getResultHistory() // Daten im Hintergrund abrufen

            if (raceResults.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, getString(R.string.no_data_to_export), Toast.LENGTH_LONG).show()
                }
                return
            }

            val csvData = convertRaceResultsToCsv(raceResults)

            // In die Datei schreiben (auf einem Hintergrundthread)
            withContext(Dispatchers.IO) {
                try {
                    contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.bufferedWriter().use { writer ->
                            writer.write(csvData)
                        }
                    }
                    // Erfolg auf dem UI-Thread anzeigen
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, getString(R.string.export_successful), Toast.LENGTH_LONG).show()
                    }
                } catch (e: IOException) {
                    Log.e("ExportCSV", "Error writing CSV file", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, getString(R.string.export_failed), Toast.LENGTH_LONG).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ExportCSV", "Error fetching data for CSV export", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, getString(R.string.export_data_fetch_failed), Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun convertRaceResultsToCsv(raceResults: List<ResultHistory>): String {
        val stringBuilder = StringBuilder()

        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // CSV-Header (passen Sie dies an die Spalten Ihrer RaceResult-Entität an)
        // WICHTIG: Die Reihenfolge muss konsistent sein!
        stringBuilder.append("onlineSessionId;onlineSessionCreationDate;onlineSessionCategory;creationDate;engineClass;drivingFromTrackName;drivingToTrackName;knockoutCupName;position;\n")

        // CSV-Datenzeilen
        raceResults.forEach { result ->
            stringBuilder.append("${result.onlineSessionId};")
            val formattedSessionCreationDate = if (result.onlineSessionCreationDate != null && result.onlineSessionCreationDate > 0) {
                outputDateFormat.format(Date(result.onlineSessionCreationDate))
            } else {
                ""
            }
            val formattedCreationDate = if (result.creationDate != null && result.creationDate > 0) {
                outputDateFormat.format(Date(result.creationDate))
            } else {
                ""
            }

            stringBuilder.append("${formattedSessionCreationDate};")
            stringBuilder.append("${result.onlineSessionCategory.name};")
            stringBuilder.append("${formattedCreationDate};")
            stringBuilder.append("${result.engineClass.name};")
            stringBuilder.append("${result.drivingFromTrackName};")
            stringBuilder.append("${result.drivingToTrackName};")
            stringBuilder.append("${result.knockoutCupName};")
            stringBuilder.append("${result.position};\n") // Letzte Spalte, dann Zeilenumbruch
        }
        return stringBuilder.toString()
    }

    /**
     * Escapes special characters for CSV fields.
     * Specifically, it doubles any double quotes within the field
     * and wraps the field in double quotes if it contains a comma, a double quote, or a newline.
     */
    private fun escapeCsvField(field: String?): String {
        if (field == null) {
            return ""
        }
        var escapedField = field.replace("\"", "\"\"") // Double up existing quotes
        if (field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r")) {
            escapedField = "\"$escapedField\"" // Wrap in quotes if it contains comma, quote, or newline
        }
        return escapedField
    }
}