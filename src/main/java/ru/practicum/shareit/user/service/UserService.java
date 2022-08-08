package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Collection<UserDto> getAllUsers();

    UserDto saveUser(UserDto userDto);

    Optional<UserDto> get(Integer id);

    void deleteUser(Integer id);

    UserDto put(Integer userId, UserDto userDto);
}
