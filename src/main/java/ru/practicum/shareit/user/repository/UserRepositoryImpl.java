package ru.practicum.shareit.user.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;


@Component
public class UserRepositoryImpl implements UserRepository {
    private final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private Integer idCounter = 1;
    private Map<Integer, User> users = new HashMap<>();
    private UserMapper userMapper;

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Autowired
    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Integer getIdCounter() {
        return idCounter++;
    }

    @Override
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @Override
    public User save(User user) {
        validateUser(user);
        user.setId(getIdCounter());
        log.debug("Пользователь сохранен");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> get(Integer id) {
        return Optional.of(users.get(id));
    }

    @Override
    public void deleteUser(Integer id) {
        if (users.containsKey(id)) {
            log.debug("Пользователь удален");
            users.remove(id);
        } else {
            log.error("Пользователя нет");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public User put(Integer userId, User user) {
        if (users.containsKey(userId)) {
            user.setId(userId);
            User userOld = users.get(userId);
            userMapper.updateUserFromUserDto(userMapper.toUserDto(user), userOld);
            log.info("Пользователь изменен");
            users.put(userId, userOld);
            return userOld;
        } else {
            log.error("Пользователя нет");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User validateUser(User user) {
        if (users.containsKey(user.getId())) {
            log.error("Пользователь такой уже есть");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (user.getEmail().length() == 0) {
            log.error("Пустой Email");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                log.error("Пользователь такой уже есть");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return user;
    }
}
