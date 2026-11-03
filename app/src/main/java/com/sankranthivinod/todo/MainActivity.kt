package com.sankranthivinod.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.sankranthivinod.todo.ui.TodoListScreen
import com.sankranthivinod.todo.ui.TodoViewModel
import com.sankranthivinod.todo.ui.theme.ToDoTheme

class MainActivity : ComponentActivity() {
    private val todoViewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoTheme {
                TodoListScreen(
                    viewModel = todoViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}