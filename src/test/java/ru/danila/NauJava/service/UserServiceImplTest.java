package ru.danila.NauJava.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.danila.NauJava.dao.UserRepository;
import ru.danila.NauJava.entity.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для UserServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setFirstName("Тест");
        testUser.setLastName("Пользователь");
        testUser.addRole("USER");
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // When
        User result = userService.findByUsername("testuser");

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("Тест", result.getFirstName());
    }

    @Test
    void findByUsername_ShouldReturnNull_WhenUserNotExists() {
        // Given
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        // When
        User result = userService.findByUsername("unknown");

        // Then
        assertNull(result);
    }

    @Test
    void saveUser_ShouldCallRepository() {
        // When
        userService.saveUser(testUser);

        // Then
        verify(userRepository, times(1)).create(testUser);
    }

    @Test
    void userExists_ShouldReturnTrue_WhenUserExists() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // When
        boolean result = userService.userExists("testuser");

        // Then
        assertTrue(result);
    }

    @Test
    void userExists_ShouldReturnFalse_WhenUserNotExists() {
        // Given
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        // When
        boolean result = userService.userExists("unknown");

        // Then
        assertFalse(result);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        User user2 = new User();
        user2.setUsername("user2");
        List<User> users = Arrays.asList(testUser, user2);

        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsers() {
        // Given
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
