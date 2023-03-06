package com.testo.service;

import com.testo.model.Todo;
import com.testo.payload.ApiResponse;
import com.testo.payload.PagedResponse;
import com.testo.security.UserPrincipal;

public interface TodoService {
	Todo completeTodo(Long id, UserPrincipal currentUser);

	Todo unCompleteTodo(Long id, UserPrincipal currentUser);

	PagedResponse<Todo> getAllTodos(int page, int size, UserPrincipal currentUser);

	Todo addTodo(Todo todo, UserPrincipal currentUser);

	Todo getTodo(Long id, UserPrincipal currentUser);

	Todo updateTodo(Long id, Todo newTodo, UserPrincipal currentUser);

	ApiResponse deleteTodo(Long id, UserPrincipal currentUser);
}
