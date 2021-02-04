package com.thuraaung.todo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thuraaung.todo.data.Task
import com.thuraaung.todo.databinding.LayoutTaskItemBinding
import com.thuraaung.todo.util.MainEvent
import com.thuraaung.todo.util.showStrikeThrough
import com.thuraaung.todo.vm.MainViewModel


class TasksAdapter(private val viewModel : MainViewModel) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutTaskItemBinding.inflate(layoutInflater,parent,false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(viewModel,getItem(position))
    }

    class TaskViewHolder(private val binding : LayoutTaskItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel : MainViewModel,task : Task) {
            binding.cbTask.isChecked = task.isCompleted == 1
            binding.cbTask.setOnCheckedChangeListener { _ , isChecked ->
                viewModel.process(MainEvent.CompletedTask(task.id,isChecked))
            }
            binding.tvTaskTitle.apply {
                text = task.name
                showStrikeThrough(!task.isActive)
            }
        }
    }
}

class TaskDiffUtilCallback : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}