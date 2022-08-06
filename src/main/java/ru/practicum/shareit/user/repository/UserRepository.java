package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface UserRepository {
    Map<Integer, User> getUsers();

    Collection<User> findAll();
    User save(User user);

    Optional<User> get(Integer id);

    void deleteUser(Integer id);

    User put(Integer userId,User user);
}
