package com.testo.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.testo.exception.ResourceNotFoundException;
import com.testo.exception.UnauthorizedException;
import com.testo.model.Todo;
import com.testo.model.role.RoleName;
import com.testo.model.user.User;
import com.testo.payload.ApiResponse;
import com.testo.payload.PagedResponse;
import com.testo.repository.TodoRepository;
import com.testo.repository.UserRepository;
import com.testo.security.UserPrincipal;
import com.testo.service.TodoService;
import com.testo.utils.AppConstants;
import com.testo.utils.AppUtils;

@Service
public class TodoServiceImpl implements TodoService {
	private static final String CREATED_AT = "createdAt";

	@Autowired
	private TodoRepository rep;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Todo completeTodo(Long id, UserPrincipal currentUser) {
		Todo todo = rep.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TODO, AppConstants.ID, id));

		User user = userRepository.getUser(currentUser);

		if (todo.getUser().getId().equals(user.getId())) {
			todo.setCompleted(Boolean.TRUE);
			return rep.save(todo);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
				AppConstants.YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public Todo unCompleteTodo(Long id, UserPrincipal currentUser) {
		Todo todo = rep.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TODO, AppConstants.ID, id));

		User user = userRepository.getUser(currentUser);

		if (todo.getUser().getId().equals(user.getId())) {
			todo.setCompleted(Boolean.FALSE);
			return rep.save(todo);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
				AppConstants.YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public PagedResponse<Todo> getAllTodos(int page, int size, UserPrincipal currentUser) {
		AppUtils.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
		Page<Todo> todos = rep.findAll(pageable);
		if (todos.getNumberOfElements() == 0)
			return new PagedResponse<>(Collections.emptyList(), todos.getNumber(), todos.getSize(),
					todos.getTotalElements(), todos.getTotalPages(), todos.isLast());

		List<Todo> tagResponses = todos.getNumberOfElements() == 0 ? Collections.emptyList() : todos.getContent();
		return new PagedResponse<>(tagResponses, todos.getNumber(), todos.getSize(), todos.getTotalElements(),
				todos.getTotalPages(), todos.isLast());
	}

	@Override
	public Todo addTodo(Todo todo, UserPrincipal currentUser) {
		User user = userRepository.getUser(currentUser);
		todo.setUser(user);
		return rep.save(todo);
	}

	@Override
	public Todo getTodo(Long id, UserPrincipal currentUser) {
		Todo t = rep.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));
		return t;
	}

	@Override
	public Todo updateTodo(Long id, Todo newTodo, UserPrincipal currentUser) {
		Todo t = rep.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
		if (t.getCreatedBy().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			t.setTitle(newTodo.getTitle());
			t.setCompleted(newTodo.getCompleted());
			return rep.save(t);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to edit this todo");
		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public ApiResponse deleteTodo(Long id, UserPrincipal currentUser) {
		Todo t = rep.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
		if (t.getCreatedBy().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			rep.deleteById(id);
			return new ApiResponse(Boolean.TRUE, "You successfully deleted a todo");
		}
		throw new UnauthorizedException("You don't have permission to delete this todo");
	}
}