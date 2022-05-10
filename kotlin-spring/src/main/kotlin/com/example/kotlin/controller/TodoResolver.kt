package com.example.kotlin.controller

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.example.kotlin.repository.Todo
import com.example.kotlin.service.TodoService
import org.springframework.stereotype.Component

@Component
class TodoResolver(
        private val todoService: TodoService
) : GraphQLQueryResolver{
    fun getTodos(): List<Todo> {
        return todoService.getTodos().toList()
    }


}