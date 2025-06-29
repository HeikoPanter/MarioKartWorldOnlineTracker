package com.rain.mariokartworldonlinetracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView // Wird möglicherweise nicht mehr benötigt, wenn Sie nur die Buttons verwenden
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
// Import für MaterialButton, falls noch nicht vorhanden
// import com.google.android.material.button.MaterialButton
import com.rain.mariokartworldonlinetracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Der Code für das TextView kann bleiben, wenn Sie es noch verwenden
        // val textView: TextView = binding.textHome
        // homeViewModel.text.observe(viewLifecycleOwner) {
        //     textView.text = it
        // }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Direkter Zugriff auf die Buttons über das binding-Objekt
        // Die IDs (button_new_race, button_new_knockout) werden automatisch
        // in CamelCase-Properties im binding-Objekt umgewandelt.
        binding.buttonNewRace.setOnClickListener {
            // Aktion für "New race" Button
            // z.B. Log.d("HomeFragment", "New Race Button clicked")
        }

        binding.buttonNewKnockout.setOnClickListener {
            // Aktion für "New knockout" Button
            // z.B. Log.d("HomeFragment", "New Knockout Button clicked")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Wichtig, um Memory Leaks zu vermeiden
    }
}