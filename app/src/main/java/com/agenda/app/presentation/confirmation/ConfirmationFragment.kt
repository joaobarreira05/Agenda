package com.agenda.app.presentation.confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.agenda.app.databinding.FragmentConfirmationBinding
import com.agenda.app.domain.model.Category
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ConfirmationFragment : Fragment() {

    private var _binding: FragmentConfirmationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ConfirmationViewModel by viewModels()
    private val args: ConfirmationFragmentArgs by navArgs()

    private var currentDateTime: LocalDateTime = LocalDateTime.now()
    private var currentCategories: List<Category> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.loadData(args.transcribedText, args.reminderId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ConfirmationUiState.Loading -> {
                            // Can show spinner
                        }
                        is ConfirmationUiState.Success -> {
                            populateUi(state)
                        }
                        is ConfirmationUiState.Saved -> {
                            findNavController().navigateUp() // Returns to Home if configured correctly
                        }
                    }
                }
            }
        }

        binding.fabSave.setOnClickListener {
            val description = binding.etDescription.text.toString()
            val selectedCategoryPos = binding.spinnerCategory.selectedItemPosition
            val categoryId = currentCategories.getOrNull(selectedCategoryPos)?.id ?: return@setOnClickListener
            
            viewModel.saveReminder(description, categoryId, currentDateTime)
        }
    }

    private fun populateUi(state: ConfirmationUiState.Success) {
        binding.tvRawText.text = "\"${state.transcribedText}\""
        if (binding.etDescription.text.isNullOrBlank()) {
            binding.etDescription.setText(state.description)
        }
        
        currentDateTime = state.dateTime
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        
        binding.btnDate.text = currentDateTime.format(dateFormatter)
        binding.btnTime.text = currentDateTime.format(timeFormatter)
        
        binding.btnDate.setOnClickListener {
            android.app.DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    currentDateTime = currentDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth)
                    binding.btnDate.text = currentDateTime.format(dateFormatter)
                },
                currentDateTime.year,
                currentDateTime.monthValue - 1,
                currentDateTime.dayOfMonth
            ).show()
        }

        binding.btnTime.setOnClickListener {
            android.app.TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    currentDateTime = currentDateTime.withHour(hourOfDay).withMinute(minute)
                    binding.btnTime.text = currentDateTime.format(timeFormatter)
                },
                currentDateTime.hour,
                currentDateTime.minute,
                true
            ).show()
        }
        
        currentCategories = state.categories
        val categoryNames = currentCategories.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter
        
        val selectedIndex = currentCategories.indexOfFirst { it.id == state.categoryId }
        if (selectedIndex >= 0) {
            binding.spinnerCategory.setSelection(selectedIndex)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
