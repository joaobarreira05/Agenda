package com.agenda.app.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agenda.app.R
import com.agenda.app.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var reminderAdapter: ReminderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.toolbar.inflateMenu(com.agenda.app.R.menu.menu_home)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                com.agenda.app.R.id.action_settings -> {
                    findNavController().navigate(com.agenda.app.R.id.action_homeFragment_to_settingsFragment)
                    true
                }
                else -> false
            }
        }

        binding.fabRecord.setOnClickListener {
            findNavController().navigate(com.agenda.app.R.id.action_homeFragment_to_recordFragment)
        }
        
        binding.fabComplete.setOnClickListener {
            val count = viewModel.selectedReminderIds.value.size
            val futureAlarmsCount = viewModel.getSelectedRemindersWithFutureAlarms()
            
            var message = "Tens a certeza que queres dar estas $count tarefas como concluídas?"
            if (futureAlarmsCount > 0) {
                message += "\n\nAtenção: algumas destas tarefas ainda não tocaram o alarme!"
            }
            
            com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Concluir Tarefas")
                .setMessage(message)
                .setPositiveButton("Sim") { _, _ ->
                    viewModel.completeSelectedReminders()
                }
                .setNegativeButton("Não", null)
                .show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.reminders.collect { reminders ->
                        reminderAdapter.submitList(reminders)
                    }
                }
                
                launch {
                    viewModel.selectedReminderIds.collect { selectedIds ->
                        reminderAdapter.selectedIds = selectedIds
                        if (selectedIds.isEmpty()) {
                            binding.fabComplete.visibility = View.GONE
                        } else {
                            binding.fabComplete.visibility = View.VISIBLE
                            binding.fabComplete.text = "Visto (${selectedIds.size})"
                        }
                    }
                }
                
                launch {
                    viewModel.categories.collect { categories ->
                        reminderAdapter.categoriesMap = categories.associate { it.id to it.color }
                        
                        val currentTabCount = binding.tabCategories.tabCount
                        if (currentTabCount == 0 && categories.isNotEmpty()) {
                            // Add 'Todas'
                            binding.tabCategories.addTab(binding.tabCategories.newTab().setText("Todas").setTag(null))
                            
                            categories.forEach { category ->
                                binding.tabCategories.addTab(binding.tabCategories.newTab().setText(category.name).setTag(category.id))
                            }
                            
                            binding.tabCategories.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
                                override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab) {
                                    val categoryId = tab.tag as? String
                                    viewModel.selectCategory(categoryId)
                                }
                                override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab) {}
                                override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab) {}
                            })
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        reminderAdapter = ReminderAdapter(
            onReminderClick = { reminder ->
                val bundle = Bundle().apply {
                    putString("transcribedText", null)
                    putString("reminderId", reminder.id)
                }
                findNavController().navigate(com.agenda.app.R.id.action_homeFragment_to_confirmationFragment, bundle)
            },
            onCompleteClick = { reminder, _ ->
                viewModel.toggleReminderSelection(reminder.id)
            }
        )
        binding.rvReminders.apply {
            adapter = reminderAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
