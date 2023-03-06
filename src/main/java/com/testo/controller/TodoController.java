package com.testo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.testo.model.Todo;
import com.testo.payload.ApiResponse;
import com.testo.payload.PagedResponse;
import com.testo.security.CurrentUser;
import com.testo.security.UserPrincipal;
import com.testo.service.TodoService;
import com.testo.utils.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
	@Autowired
	private TodoService todoService;

	@GetMapping
	public PagedResponse<Todo> getAllTodos(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@CurrentUser UserPrincipal currentUser) {
		return todoService.getAllTodos(page, size, currentUser);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Todo> getTodo(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
		return new ResponseEntity<>(todoService.getTodo(id, currentUser), HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Todo> addTodo(@Valid @RequestBody Todo todo, @CurrentUser UserPrincipal currentUser) {
		Todo newTodo = todoService.addTodo(todo, currentUser);

		return new ResponseEntity<>(newTodo, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<Todo> updateTodo(@PathVariable(name = "id") Long id, @Valid @RequestBody Todo Todo,
			@CurrentUser UserPrincipal currentUser) {
		return new ResponseEntity<>(todoService.updateTodo(id, Todo, currentUser), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteTodo(@PathVariable(name = "id") Long id,
			@CurrentUser UserPrincipal currentUser) {
		return new ResponseEntity<>(todoService.deleteTodo(id, currentUser), HttpStatus.OK);
	}

	@PutMapping("/{id}/complete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Todo> completeTodo(@PathVariable(value = "id") Long id,
			@CurrentUser UserPrincipal currentUser) {

		Todo todo = todoService.completeTodo(id, currentUser);

		return new ResponseEntity<>(todo, HttpStatus.OK);
	}

	@PutMapping("/{id}/unComplete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Todo> unCompleteTodo(@PathVariable(value = "id") Long id,
			@CurrentUser UserPrincipal currentUser) {

		Todo todo = todoService.unCompleteTodo(id, currentUser);

		return new ResponseEntity<>(todo, HttpStatus.OK);
	}
}
