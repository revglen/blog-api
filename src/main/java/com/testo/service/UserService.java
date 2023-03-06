package com.testo.service;

import com.testo.model.user.User;
import com.testo.payload.ApiResponse;
import com.testo.payload.InfoRequest;
import com.testo.payload.UserIdentityAvailability;
import com.testo.payload.UserProfile;
import com.testo.payload.UserSummary;
import com.testo.security.UserPrincipal;

public interface UserService {
	UserSummary getCurrentUser(UserPrincipal currentUser);

	UserIdentityAvailability checkUsernameAvailability(String username);

	UserIdentityAvailability checkEmailAvailability(String username);

	UserProfile getUserProfile(String username);

	User addUser(User user);

	User updateUser(User newUser, String username, UserPrincipal currentUser);

	ApiResponse deleteUser(String username, UserPrincipal cuurentUser);

	ApiResponse giveAdmin(String username);

	ApiResponse removeAdmin(String username);

	UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest);
}
