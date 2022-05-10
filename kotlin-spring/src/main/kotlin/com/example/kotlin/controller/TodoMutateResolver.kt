package com.example.kotlin.controller

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.example.kotlin.repository.Todo
import com.example.kotlin.service.TodoService
import org.springframework.stereotype.Component

@Component
class TodoMutateResolver(
        private val todoService: TodoService
):GraphQLMutationResolver {
    fun createTodo(todoRequest: TodoRequest): Todo {
        return todoService.insertTodo(todoRequest.todoName)
    }
}