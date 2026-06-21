package com.bablumia.store.users;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers(String sort) {

        if (!Set.of("name", "email").contains(sort))
            sort = "name";

        return userRepository
                .findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUserById(Long id) {
        return userRepository
                .findById(id)
                .map(userMapper::toDto)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserDto registerUser(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException();
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    public UserDto updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        userMapper.update(request, user);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    public void changePassword(Long id, ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new AccessDeniedException("Old password does not match");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

}
