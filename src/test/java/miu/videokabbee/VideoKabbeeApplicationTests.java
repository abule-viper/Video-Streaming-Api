package miu.videokabbee;

import miu.videokabbee.ExceptionHandling.ExceptionHandling;
import miu.videokabbee.controller.UserController;
import miu.videokabbee.domain.Contact;
import miu.videokabbee.domain.Users;
import miu.videokabbee.service.UserServiceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoKabbeeApplicationTests {

	@InjectMocks
	UserController userController;

	@Mock
	UserServiceImpl userService;

	@Mock
	PasswordEncoder passwordEncoder;

	@Test
	void getUserByID_returnsValidUser() {
		Users users = new Users();
		users.setId(1L);
		users.setFirstName("Test User");
		users.setContact(new Contact());
		users.getContact().setEmail("testuser@test.com");

		when(userService.findById(1L)).thenReturn(users);

		ResponseEntity<?> response = userController.getUserByID(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(users, response.getBody());
	}

	@Test
	void getUserByID_returnsNotFoundForInvalidUser() {
		when(userService.findById(1000L)).thenReturn(null);

		ResponseEntity<?> response = userController.getUserByID(1000L);

		assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
		assertThat(response.getBody(), is(new ExceptionHandling("not available")));
	}

	@Test
	void registerUser_registersValidUser() {
		Users users = new Users();
		users.setFirstName("Test User");
		users.setContact(new Contact()); // Initialize the Contact object
		users.getContact().setEmail("testuser@test.com");
		users.setPassword("testpassword");
		users.setUserName("Tes");

		String encodedPassword = "encodedPassword";
		users.setPassword(encodedPassword);

		when(passwordEncoder.encode(users.getPassword())).thenReturn(encodedPassword);
		when(userService.register(users)).thenReturn("Success");


		ResponseEntity<?> response = userController.registerUser(users);

		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	void InvalidaUserName() {
		Users users = new Users();
		users.setFirstName("Test User");
		users.setContact(new Contact());
		users.getContact().setEmail("testuser@test.com");
		users.setPassword("testpassword");
		users.setUserName("Tes");

		when(userService.register(users)).thenReturn("Username-taken");
		assertEquals("Username-taken".length(), userService.register(users).length());
	}




	@Test
	void InvalidUserEmail() {
		Users users = new Users();
		users.setFirstName("Test User");
		users.setContact(new Contact());
		users.getContact().setEmail("testuser@test.com");
		users.setPassword("testpassword");
		users.setUserName("Tes");

		when(userService.register(users)).thenReturn("Email-taken");
		assertEquals("Email-taken".length(), userService.register(users).length());
	}
}

