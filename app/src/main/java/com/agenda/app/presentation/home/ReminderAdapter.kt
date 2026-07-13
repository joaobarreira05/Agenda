package com.agenda.app.presentation.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agenda.app.databinding.ItemReminderBinding
import com.agenda.app.domain.model.Reminder
import java.time.format.DateTimeFormatter

class ReminderAdapter(
    private val onReminderClick: (Reminder) -> Unit,
    private val onCompleteClick: (Reminder, Boolean) -> Unit
) : ListAdapter<Reminder, ReminderAdapter.ReminderViewHolder>(ReminderDiffCallback()) {

    var selectedIds: Set<String> = emptySet()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        
    var categoriesMap: Map<String, String> = emptyMap()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ItemReminderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReminderViewHolder(
        private val binding: ItemReminderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reminder: Reminder) {
            binding.tvDescription.text = reminder.description
            
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")
            binding.tvDateTime.text = reminder.dateTime.format(formatter)
            
            // Overdue check
            val isOverdue = reminder.dateTime.isBefore(java.time.LocalDateTime.now())
            if (isOverdue) {
                binding.tvDateTime.setTextColor(Color.parseColor("#FF5252")) // lighter red
                (binding.root as? com.google.android.material.card.MaterialCardView)?.setCardBackgroundColor(Color.parseColor("#33FF0000")) // Semi-transparent red
            } else {
                binding.tvDateTime.setTextColor(Color.parseColor("#808080")) // Fallback grey
                
                // Reset background to surface color
                val typedValue = android.util.TypedValue()
                binding.root.context.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
                (binding.root as? com.google.android.material.card.MaterialCardView)?.setCardBackgroundColor(typedValue.data)
            }
            
            binding.cbComplete.setOnCheckedChangeListener(null)
            binding.cbComplete.isChecked = selectedIds.contains(reminder.id)
            
            val colorHex = categoriesMap[reminder.categoryId] ?: "#1E88E5"
            try {
                binding.viewCategoryColor.setBackgroundColor(Color.parseColor(colorHex))
            } catch (e: Exception) {
                binding.viewCategoryColor.setBackgroundColor(Color.parseColor("#1E88E5"))
            }
            
            binding.root.setOnClickListener {
                onReminderClick(reminder)
            }
            
            binding.cbComplete.setOnCheckedChangeListener { _, _ ->
                onCompleteClick(reminder, true) // Just pass true, we only use the callback for toggling
            }
        }
    }

    class ReminderDiffCallback : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem == newItem
        }
    }
}
