package com.rain.mariokartworldonlinetracker.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.databinding.FragmentHomeBinding
import java.io.File
import java.io.IOException
import kotlin.io.path.copyTo
import kotlin.io.path.exists

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var exportDbLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ActivityResultLauncher registrieren. Dies muss in onCreate oder onAttach erfolgen.
        exportDbLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Der Benutzer hat einen Speicherort ausgewählt, jetzt die DB dorthin schreiben
                    val currentContext = context
                    if (currentContext != null) {
                        writeDatabaseToUri(currentContext, uri, "mkwot_db")
                    } else {
                        Log.e("DbExport", "Context war null nach Auswahl des Speicherorts.")
                        Toast.makeText(activity, "Fehler: Kontext nicht verfügbar nach Auswahl", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Datenbankexport abgebrochen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backupButton.setOnClickListener {
            openFilePickerForDbExport()
        }
    }

    private fun openFilePickerForDbExport() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            // Mime-Type für SQLite-Datenbanken.
            // Alternativ "application/octet-stream" für eine generische Binärdatei
            // oder "application/x-sqlite3"
            type = "application/vnd.sqlite3" // Gängiger Mime-Type für SQLite
            // type = "application/x-sqlite3" // auch oft verwendet
            // type = "application/octet-stream" // Generischer Binärtyp, falls der spezifische nicht gut funktioniert

            // Vorschlag für den Dateinamen
            putExtra(Intent.EXTRA_TITLE, "mkwot_db_sicherung")

            // Optional: Startverzeichnis vorschlagen (funktioniert nicht auf allen Android-Versionen/Geräten gleich)
            // val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            // putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.fromFile(downloadsDir))
        }
        try {
            exportDbLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Dateiauswahl konnte nicht gestartet werden: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("DbExport", "Fehler beim Starten des Dateiauswahldialogs", e)
        }
    }

    private fun writeDatabaseToUri(context: Context, destinationUri: Uri, databaseName: String) {
        val dbFile = context.getDatabasePath(databaseName)
        if (!dbFile.exists()) {
            Log.e("DbExport", "Datenbank nicht gefunden: $databaseName unter Pfad ${dbFile.absolutePath}")
            Toast.makeText(context, "Datenbank '${databaseName}' nicht gefunden", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Öffne einen OutputStream zur vom Benutzer gewählten URI
            context.contentResolver.openOutputStream(destinationUri).use { outputStream ->
                if (outputStream == null) {
                    Log.e("DbExport", "Konnte keinen OutputStream für die URI erstellen: $destinationUri")
                    Toast.makeText(context, "Fehler beim Öffnen des Speicherorts", Toast.LENGTH_SHORT).show()
                    return@writeDatabaseToUri // Frühzeitiger Ausstieg aus der Funktion
                }
                java.io.FileInputStream(dbFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                    Log.i("DbExport", "Datenbank erfolgreich exportiert nach: $destinationUri")
                    Toast.makeText(context, "Datenbank exportiert!", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: IOException) {
            Log.e("DbExport", "Fehler beim Exportieren der Datenbank zur URI", e)
            Toast.makeText(context, "Fehler beim Exportieren: ${e.message}", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Log.e("DbExport", "Sicherheitsausnahme beim Schreiben zur URI", e)
            Toast.makeText(context, "Fehler: Keine Berechtigung zum Schreiben am gewählten Ort.", Toast.LENGTH_LONG).show()
        }
    }
}