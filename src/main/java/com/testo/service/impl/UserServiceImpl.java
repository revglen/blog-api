package com.testo.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.testo.exception.AccessDeniedException;
import com.testo.exception.AppException;
import com.testo.exception.BadRequestException;
import com.testo.exception.ResourceNotFoundException;
import com.testo.exception.UnauthorizedException;
import com.testo.model.role.Role;
import com.testo.model.role.RoleName;
import com.testo.model.user.Address;
import com.testo.model.user.Company;
import com.testo.model.user.Geo;
import com.testo.model.user.User;
import com.testo.payload.ApiResponse;
import com.testo.payload.InfoRequest;
import com.testo.payload.UserIdentityAvailability;
import com.testo.payload.UserProfile;
import com.testo.payload.UserSummary;
import com.testo.repository.PostRepository;
import com.testo.repository.RoleRepository;
import com.testo.repository.UserRepository;
import com.testo.security.UserPrincipal;
import com.testo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserSummary getCurrentUser(UserPrincipal currentUser) {
		return new UserSummary(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName(),
				currentUser.getUsername());
	}

	@Override
	public UserIdentityAvailability checkUsernameAvailability(String username) {
		return new UserIdentityAvailability(userRepository.existsByUsername(username));
	}

	@Override
	public UserIdentityAvailability checkEmailAvailability(String username) {
		return new UserIdentityAvailability(userRepository.existsByEmail(username));
	}

	@Override
	public UserProfile getUserProfile(String username) {
		User user = userRepository.getUserByName(username);

		Long postCount = postRepository.countByCreatedBy(user.getId());
		UserProfile u = new UserProfile(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
				user.getCreatedAt(), user.getEmail(), user.getAddress(), user.getPhone(), user.getWebsite(),
				user.getCompany(), postCount);

		return u;
	}

	@Override
	public User addUser(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Username is already taken");
			throw new BadRequestException(apiResponse);
		}

		if (userRepository.existsByEmail(user.getEmail())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email is already taken");
			throw new BadRequestException(apiResponse);
		}

		ArrayList<Role> roles = new ArrayList<>();
		roles.add(
				roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
		user.setRoles(roles);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User newUser, String username, UserPrincipal currentUser) {
		User user = userRepository.getUserByName(username);
		if (user.getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			user.setFirstName(newUser.getFirstName());
			user.setLastName(newUser.getLastName());
			user.setPassword(passwordEncoder.encode(newUser.getPassword()));
			user.setAddress(newUser.getAddress());
			user.setPhone(newUser.getPhone());
			user.setWebsite(newUser.getWebsite());
			user.setCompany(newUser.getCompany());

			return userRepository.save(user);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
				"You don't have permission to update profile of: " + username);
		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));
		if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			userRepository.deleteById(user.getId());
			return new ApiResponse(Boolean.TRUE, "You successfully deleted a post");
		}
		throw new UnauthorizedException("You don't have permission to delete this User");
	}

	@Override
	public ApiResponse giveAdmin(String username) {
		User user = userRepository.getUserByName(username);
		for (Role r : user.getRoles()) {
			if (r.getName() == RoleName.ROLE_ADMIN) {
				return new ApiResponse(Boolean.TRUE, "ADMIN role already presnt with user: " + username);
			}
		}

		ArrayList<Role> roles = new ArrayList<>();
		roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
				.orElseThrow(() -> new AppException("Admin role not set")));
		roles.add(roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new AppException("Admin role not set")));

		user.setRoles(roles);
		// userRepository.save(user);
		userRepository.saveAndFlush(user);
		return new ApiResponse(Boolean.TRUE, "You gave ADMIN role to user: " + username);
	}

	@Override
	public ApiResponse removeAdmin(String username) {
		User user = userRepository.getUserByName(username);
		ArrayList<Role> roles = new ArrayList<>();
		roles.add(
				roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
		user.setRoles(roles);
		userRepository.saveAndFlush(user);
		return new ApiResponse(Boolean.TRUE, "You took ADMIN role from user: " + username);
	}

	@Override
	public UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest) {
		User user = userRepository.findByUsername(currentUser.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));
		Geo geo = new Geo(infoRequest.getLat(), infoRequest.getLng());
		Address address = new Address(infoRequest.getCompanyName(), infoRequest.getCatchPhrase(), infoRequest.getCity(),
				infoRequest.getZipcode(), geo);
		Company company = new Company(infoRequest.getCompanyName(), infoRequest.getCatchPhrase(), infoRequest.getBs());

		if (user.getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			user.setAddress(address);
			user.setCompany(company);
			user.setWebsite(infoRequest.getWebsite());
			user.setPhone(infoRequest.getPhone());
			User updatedUser = userRepository.save(user);

			Long postCount = postRepository.countByCreatedBy(updatedUser.getId());

			return new UserProfile(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getFirstName(),
					updatedUser.getLastName(), updatedUser.getCreatedAt(), updatedUser.getEmail(),
					updatedUser.getAddress(), updatedUser.getPhone(), updatedUser.getWebsite(),
					updatedUser.getCompany(), postCount);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update users profile",
				HttpStatus.FORBIDDEN);
		throw new AccessDeniedException(apiResponse);
	}
}