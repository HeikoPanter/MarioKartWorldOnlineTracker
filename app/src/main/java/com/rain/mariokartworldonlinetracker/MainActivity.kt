package com.rain.mariokartworldonlinetracker

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.rain.mariokartworldonlinetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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
                R.id.nav_home, R.id.nav_start_session, R.id.nav_statistics
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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
            .setPositiveButton("OK") { dialog, _ ->
                val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                if (selectedPosition != -1) {
                    val selectedValue = options[selectedPosition].toIntOrNull()
                    selectedValue?.let {
                        MkwotSettings.saveItemsPerRow(applicationContext, it)
                        // HIER: UI benachrichtigen, dass sich die Einstellung geändert hat,
                        // damit sich z.B. ein RecyclerView neu anordnet.
                        // Das kann über ein LiveData im ViewModel geschehen,
                        // das von der Activity/Fragment beobachtet wird, oder durch einen direkten Aufruf
                        // einer Methode, die das Layout aktualisiert.
                        Toast.makeText(this, "Changed items per row to $it", Toast.LENGTH_SHORT).show()
                        // Beispiel: recreate() // Startet die Activity neu, um Änderungen zu sehen (einfachste, aber nicht immer beste Lösung)
                        // Besser: Spezifische UI-Komponenten gezielt aktualisieren.
                        recreate()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}