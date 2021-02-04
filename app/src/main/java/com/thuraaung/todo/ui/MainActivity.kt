package com.thuraaung.todo.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.thuraaung.mvi.AacMviActivity
import com.thuraaung.todo.*
import com.thuraaung.todo.data.Task
import com.thuraaung.todo.data.TasksFilterType
import com.thuraaung.todo.databinding.ActivityMainBinding
import com.thuraaung.todo.util.*
import com.thuraaung.todo.vm.MainViewModel

class MainActivity : AacMviActivity<MainState, MainEffect, MainEvent, MainViewModel>() {

    override val viewModel: MainViewModel by viewModels { getViewModelFactory() }
    private lateinit var tasksAdapter : TasksAdapter

    private lateinit var binding : ActivityMainBinding

    override fun renderViewState(viewState: MainState) {
        when(viewState.status) {
            LoadStatus.NotLoad -> {
                viewModel.process(MainEvent.LoadTasks)
                binding.content.swTask.isRefreshing = false
                binding.content.rvTask.visibility = View.GONE
            }
            LoadStatus.Loading -> {
                binding.content.rvTask.visibility = View.GONE
                binding.content.swTask.isRefreshing = true
            }
            LoadStatus.Loaded -> {
                binding.content.rvTask.visibility = View.VISIBLE
                binding.content.swTask.isRefreshing = false
            }
        }
        tasksAdapter.submitList(viewState.tasks)
    }

    override fun renderViewEffect(viewEffect: MainEffect) {
        when(viewEffect) {
            is MainEffect.ShowToast -> Toast.makeText(this,viewEffect.message,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        viewModel.tasksTitle.observe(this, { tasksTitle ->
            binding.content.tvTaskTitle.text = tasksTitle
        })

        binding.content.swTask.setOnRefreshListener {
            viewModel.process(MainEvent.RefreshTasks)
        }

        tasksAdapter = TasksAdapter(viewModel)
        binding.content.rvTask.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = tasksAdapter
        }

        binding.content.etTask.setOnEditorActionListener { _, actionId, _ ->

            val etTask = binding.content.etTask
            val taskName = etTask.text?.toString() ?: ""

            if (actionId == EditorInfo.IME_ACTION_DONE && taskName.isNotEmpty()) {
                viewModel.process(MainEvent.AddNewTask(taskName))
                etTask.text!!.clear()
                binding.content.rvTask.smoothScrollToPosition(0)
                true

            } else false
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter_tasks,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val filterType = when(item.itemId) {
            R.id.all -> TasksFilterType.ALL
            R.id.active -> TasksFilterType.ACTIVE
            R.id.completed -> TasksFilterType.COMPLETE
            else -> TasksFilterType.ALL
        }
        viewModel.process(MainEvent.FilterTask(filterType))
        return super.onOptionsItemSelected(item)
    }
}