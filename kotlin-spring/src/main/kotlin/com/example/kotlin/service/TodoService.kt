package com.example.kotlin.service

import com.example.kotlin.repository.Todo
import com.example.kotlin.repository.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class TodoService(
        private val todoRepository: TodoRepository
) {
    fun getTodos() = todoRepository.findAll()
    fun insertTodo(todoName:String) = todoRepository.save(Todo(todoName = todoName))
    fun updateTodo(todoId: Long): Todo{
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw Exception()
        todo.completed = !todo.completed //상태 반대로 바꿔준다.

        return todoRepository.save(todo)
    }

    fun deleteTodo(todoId: Long) = todoRepository.deleteById(todoId)
}