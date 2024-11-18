package com.example.springmvc.controller;

import entity.User;
import com.example.springmvc.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test Testovich");
        user.setEmail("test.test@test.com");
    }

    @Test
    void testGetAllUsers_WithUserSummaryView() throws Exception {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$[0].orders").doesNotExist());
    }

    @Test
    void testGetUserById_WithUserDetailsView() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        mockMvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.orders").exists());
    }

    @Test
    void testCreateUser() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Test Testovich");
        updatedUser.setEmail("test.test@test.com");

        mockMvc.perform(put("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()));
    }

    @Test
    void testDeleteUser() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/users/{id}", 1)).andExpect(status().isNoContent());
    }
}

