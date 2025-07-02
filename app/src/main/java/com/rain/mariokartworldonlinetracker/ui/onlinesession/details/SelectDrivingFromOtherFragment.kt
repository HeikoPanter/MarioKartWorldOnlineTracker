package com.rain.mariokartworldonlinetracker.ui.onlinesession.details

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.rain.mariokartworldonlinetracker.MarioKartWorldOnlineTrackerApplication
import com.rain.mariokartworldonlinetracker.R
import com.rain.mariokartworldonlinetracker.RaceResultRepository
import com.rain.mariokartworldonlinetracker.TrackAndKnockoutHelper
import com.rain.mariokartworldonlinetracker.TrackName
import com.rain.mariokartworldonlinetracker.databinding.FragmentSelectDrivingFromOtherBinding
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModel
import com.rain.mariokartworldonlinetracker.ui.onlinesession.NewOnlineSessionViewModelFactory

class SelectDrivingFromOtherFragment : Fragment() {

    private var _binding: FragmentSelectDrivingFromOtherBinding? = null
    private lateinit var newOnlineSessionViewModel: NewOnlineSessionViewModel

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectDrivingFromOtherBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val raceResultDao = (requireActivity().application as MarioKartWorldOnlineTrackerApplication).database.raceResultDao()
        val repository = RaceResultRepository(raceResultDao)
        newOnlineSessionViewModel = ViewModelProvider(
            requireActivity(),
            NewOnlineSessionViewModelFactory(repository))
            .get(NewOnlineSessionViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackOptions = TrackAndKnockoutHelper.getPossibleTracks(null)
        val itemsPerRow = 3
        var currentRowLinearLayout: LinearLayout? = null

        val context = requireContext() // Kontext einmal holen

        // Definiere den gewünschten Margin einmal, um Konsistenz zu gewährleisten
        val horizontalImageMarginDp = 4
        val horizontalImageMarginPx = (horizontalImageMarginDp * resources.displayMetrics.density).toInt()

        // Der vertikale Margin für die Reihe soll ähnlich sein
        // Wenn die Bilder oben und unten je 4dp Margin haben, ist der Abstand zwischen den Bildinhalten 8dp.
        // Wir wollen, dass der Abstand zwischen den Reihen-Containern so ist, dass es passt.
        // Ein einfacher Ansatz ist, den gleichen Margin wie für die Bilder zu verwenden.
        val verticalRowMarginDp = 4 // Oder einen anderen Wert, den Sie bevorzugen
        val verticalRowMarginPx = (verticalRowMarginDp * resources.displayMetrics.density).toInt()


        trackOptions.forEachIndexed { index, trackInfo ->
            // Beginne eine neue Reihe, wenn nötig
            if (index % itemsPerRow == 0) {
                currentRowLinearLayout = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Reihe nimmt volle Breite
                        LinearLayout.LayoutParams.WRAP_CONTENT  // Höhe der Reihe passt sich Inhalt an
                    )
                    // WICHTIG: Setze weightSum, wenn die Kinder Gewichte verwenden
                    weightSum = itemsPerRow.toFloat()
                }
                // Füge die neue Reihe zum Hauptcontainer hinzu
                binding.imageViewContainer.addView(currentRowLinearLayout)
            }

            // Erstelle und konfiguriere das ImageView
            val imageView = createTrackImageView(trackInfo, horizontalImageMarginPx)
            // Füge das ImageView zur aktuellen Reihe hinzu
            currentRowLinearLayout?.addView(imageView)
        }
    }

    private fun createTrackImageView(trackName: TrackName, horizontalMarginPx: Int): ImageView {
        val context = requireContext()

        return ImageView(context).apply {
            id = View.generateViewId() // Wichtig für eindeutige Identifizierung
            setImageResource(R.drawable.mbc1)

            adjustViewBounds = true

            // LayoutParameter für das ImageView innerhalb der horizontalen Reihe
            val imageLayoutParams = LinearLayout.LayoutParams(
                0, // Breite auf 0 setzen, da wir Gewichtung verwenden
                LinearLayout.LayoutParams.WRAP_CONTENT // Höhe an Inhalt anpassen
                // ODER eine feste Höhe:
                // resources.getDimensionPixelSize(R.dimen.track_icon_height)
            ).apply {
                weight = 1f // Jedes ImageView bekommt gleiches Gewicht in der Reihe
                // Optional: Margins um jedes ImageView
                val verticalImageMarginInRowDp = 4 // Z.B. der gleiche Margin wie horizontal
                val verticalImageMarginInRowPx = (verticalImageMarginInRowDp * resources.displayMetrics.density).toInt()
                setMargins(horizontalMarginPx, verticalImageMarginInRowPx, horizontalMarginPx, verticalImageMarginInRowPx)
            }
            this.layoutParams = imageLayoutParams

            // Skalierung des Bildes innerhalb des ImageView-Bereichs
            scaleType = ImageView.ScaleType.FIT_CENTER // Oder FIT_XY, CENTER_CROP etc.
            // FIT_CENTER ist oft gut für Icons

            // Klick-Effekt (Ripple) und Klickbarkeit
            // Holt den Standard-Ripple-Effekt des Themes
            //val outValue = android.util.TypedValue()
            //context.theme.resolveAttribute(com.google.android.material.R.attr.selectableItemBackgroundBorderless, outValue, true)
            //background = ContextCompat.getDrawable(context, outValue.resourceId) // outValue.resourceId ist die tatsächliche Drawable-ID
            background = ColorDrawable(ContextCompat.getColor(context, R.color.mk_black))
            isClickable = true
            isFocusable = true // Wichtig für Tastatur-Navigation und Accessibility

            // OnClickListener
            setOnClickListener {
                navigateNext(trackName)
            }

            // Accessibility
            contentDescription = trackName.name // Wichtig für Screenreader
        }
    }

    fun navigateNext(drivingFromTrackName: TrackName) {
        newOnlineSessionViewModel.setDrivingFromTrackName(drivingFromTrackName)
        findNavController().navigate(R.id.action_selectDrivingFromOtherFragment_to_selectDrivingToFragment)
    }
}
