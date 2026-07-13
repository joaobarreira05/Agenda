package com.agenda.app.presentation.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.agenda.app.databinding.FragmentCategoryListBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryListFragment : Fragment() {

    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryListViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        categoryAdapter = CategoryAdapter { category ->
            val dialogView = LayoutInflater.from(requireContext()).inflate(com.agenda.app.R.layout.dialog_add_category, null)
            val etName = dialogView.findViewById<android.widget.EditText>(com.agenda.app.R.id.etCategoryName)
            val etColor = dialogView.findViewById<android.widget.EditText>(com.agenda.app.R.id.etCategoryColor)
            
            etName.setText(category.name)
            etColor.setText(category.color)
            
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Editar Categoria")
                .setView(dialogView)
                .setPositiveButton("Guardar") { _, _ ->
                    val name = etName.text.toString()
                    val color = etColor.text.toString().ifBlank { "#4285F4" }
                    if (name.isNotBlank()) {
                        viewModel.updateCategory(category, name, color)
                    }
                }
                .setNegativeButton("Cancelar", null)
                .setNeutralButton("Eliminar") { _, _ ->
                    viewModel.deleteCategory(category)
                }
                .show()
        }

        binding.rvCategories.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.fabAddCategory.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(com.agenda.app.R.layout.dialog_add_category, null)
            val etName = dialogView.findViewById<android.widget.EditText>(com.agenda.app.R.id.etCategoryName)
            val etColor = dialogView.findViewById<android.widget.EditText>(com.agenda.app.R.id.etCategoryColor)
            
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Nova Categoria")
                .setView(dialogView)
                .setPositiveButton("Criar") { _, _ ->
                    val name = etName.text.toString()
                    val color = etColor.text.toString().ifBlank { "#4285F4" }
                    if (name.isNotBlank()) {
                        viewModel.addCategory(name, color)
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collect { categories ->
                    categoryAdapter.submitList(categories)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
