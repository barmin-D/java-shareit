package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
class UserServiceImpl implements UserService {
    private UserRepository repository;
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserMapper userMapper) {
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
        if (userDto.getEmail()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user=userMapper.toUser(userDto);
        return userMapper.toUserDto(repository.save(user));
    }

    @Override
    public Optional<UserDto> get(Integer id) {
        Optional<User>user=repository.get(id);
        return Optional.of(userMapper.toUserDto(user.get()));
    }

    @Override
    public void deleteUser(Integer id) {
        repository.deleteUser(id);
    }

    @Override
    public UserDto put(Integer userId,UserDto  userDto ) {
        Optional<User> user=repository.get(userId);
        for (User u : repository.findAll()) {
            if (u.getEmail().equals(userDto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        userMapper.updateUserFromUserDto(userDto,user.get());
        return userMapper.toUserDto(repository.put(userId,user.get()));
    }
}
