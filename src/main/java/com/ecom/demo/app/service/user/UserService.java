package com.ecom.demo.app.service.user;

import com.ecom.demo.app.dto.user.AddressDTO;
import com.ecom.demo.app.dto.user.UserRequest;
import com.ecom.demo.app.dto.user.UserResponse;
import com.ecom.demo.app.repository.user.Address;
import com.ecom.demo.app.repository.user.User;
import com.ecom.demo.app.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<UserResponse> createUser(UserRequest userRequest) {
        User user = mapToUser(userRequest);
        user = userRepository.save(user);
        UserResponse userResponse = mapToUserResponse(user);
        return Optional.of(userResponse);
    }

    public Optional<List<UserResponse>> fetchAllUser() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream().map(this::mapToUserResponse).toList();
        return Optional.of(userResponses);
    }

    public Optional<UserResponse> getUserById(String id) {
        Optional<User> user = userRepository.findById(Long.valueOf(id));
        return user.map(this::mapToUserResponse).or(Optional::empty);
    }

    public Optional<UserResponse> updateUserById(String id, UserRequest userRequest) {
        return userRepository.findById(Long.valueOf(id)).map(user -> {
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setEmail(userRequest.getEmail());
            user.setPhone(userRequest.getPhone());
            user.getAddress().setCity(userRequest.getAddress().getCity());
            user.getAddress().setState(userRequest.getAddress().getState());
            user.getAddress().setZipcode(userRequest.getAddress().getZipcode());
            user.getAddress().setCountry(userRequest.getAddress().getCountry());
            user = userRepository.save(user);
            UserResponse userResponse = mapToUserResponse(user);
            return Optional.of(userResponse);
        }).orElse(Optional.empty());
    }

    public Optional<Boolean> deleteUserById(String id) {
        Optional<User> user = userRepository.findById(Long.valueOf(id));
        if(user.isPresent()){
            userRepository.deleteById(Long.valueOf(id));
            return Optional.of(true);
        }
        return Optional.of(false);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity(user.getAddress().getCity());
        addressDTO.setState(user.getAddress().getState());
        addressDTO.setZipcode(user.getAddress().getZipcode());
        addressDTO.setCountry(user.getAddress().getCountry());
        userResponse.setAddress(addressDTO);
        return userResponse;
    }

    private User mapToUser(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        Address address = new Address();
        address.setCity(userRequest.getAddress().getCity());
        address.setCountry(userRequest.getAddress().getCountry());
        address.setState(userRequest.getAddress().getState());
        address.setZipcode(userRequest.getAddress().getZipcode());
        user.setAddress(address);
        return user;
    }
}
