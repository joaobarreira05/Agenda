package com.agenda.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Temporary placeholder fragment for Phase 1.
 *
 * Displays a simple "Hello World" message to verify that the project
 * compiles and runs correctly.
 *
 * This fragment will be replaced by [HomeFragment] in Phase 3.
 */
@AndroidEntryPoint
class PlaceholderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TextView(requireContext()).apply {
            text = "Agenda — Projeto configurado com sucesso!"
            textSize = 18f
            setPadding(48, 48, 48, 48)
        }
    }
}
