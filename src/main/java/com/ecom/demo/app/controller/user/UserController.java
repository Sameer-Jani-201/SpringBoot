package com.ecom.demo.app.controller.user;

import com.ecom.demo.app.dto.user.UserRequest;
import com.ecom.demo.app.dto.user.UserResponse;
import com.ecom.demo.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest){
        Optional<UserResponse> response = userService.createUser(userRequest);
        return response.map(userResponse -> new ResponseEntity<>(userResponse, HttpStatus.CREATED)).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        Optional<List<UserResponse>> response = userService.fetchAllUser();
        if (response.isPresent() && response.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return response.map(userResponses -> new ResponseEntity<>(userResponses, HttpStatus.OK)).get();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> fetchUserById(@PathVariable String id){
        Optional<UserResponse> response = userService.getUserById(id);
        return response.map(userResponse -> new ResponseEntity<>(userResponse, HttpStatus.OK)).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable String id, @RequestBody UserRequest userRequest){
        Optional<UserResponse> response = userService.updateUserById(id, userRequest);
        return response.map(userResponse -> new ResponseEntity<>(userResponse, HttpStatus.OK)).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable String id){
        Optional<Boolean> isDeleted = userService.deleteUserById(id);
        return new ResponseEntity<>(isDeleted.get() ? "User deleted successfully" : "User NOT deleted", isDeleted.get() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}

