package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDbRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class UserServiceImpl implements UserService {
    private UserDbRepository repository;
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserDbRepository repository, UserMapper userMapper) {
        this.repository = repository;
        this.userMapper = userMapper;
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(repository.save(user));
    }

    @Override
    public Optional<UserDto> get(Integer id) {
        Optional<User> user = repository.findById(id);
        try {
            return Optional.of(userMapper.toUserDto(user.get()));
        } catch (Throwable e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteUser(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public UserDto put(Integer userId, UserDto userDto) {
        Optional<User> user = repository.findById(userId);
        for (User u : repository.findAll()) {
            if (u.getEmail().equals(userDto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        userMapper.updateUserFromUserDto(userDto, user.get());
        return userMapper.toUserDto(repository.save(user.get()));
    }
}
