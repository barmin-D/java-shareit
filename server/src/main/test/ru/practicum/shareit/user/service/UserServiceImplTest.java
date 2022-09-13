package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Rollback(false)
@SpringBootTest(
        properties = "db.name=test7",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final EntityManager em;
    private final UserService userService;

    @Test
    void saveUser() {
        UserDto userDto = makeUserDto(1, "user", "user@user.ru");
        userService.saveUser(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id=:id", User.class);
        User user = query.setParameter("id", userDto.getId())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void saveUserBad() {
        UserDto userDto = makeUserDto(1, "user", null);

        assertThrows(ResponseStatusException.class, () -> userService.saveUser(userDto));
    }


    @Test
    void get() {
        UserDto userDto = makeUserDto(1, "user", "user@user.ru");
        userService.saveUser(userDto);

        Optional<UserDto> optionalUserDto = userService.get(1);

        assertThat(optionalUserDto.get().getId(), notNullValue());
        assertThat(optionalUserDto.get().getName(), equalTo(userDto.getName()));
        assertThat(optionalUserDto.get().getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void getBad() {
        UserDto userDto = makeUserDto(1, "user", "user@user.ru");
        userService.saveUser(userDto);

        assertThrows(ResponseStatusException.class, () -> userService.get(199));
    }

    @Test
    void deleteUser() {
        userService.deleteUser(1);

        List<UserDto> targetUsers = (List<UserDto>) userService.getAllUsers();

        assertThat(targetUsers, hasSize(2));
    }

    @Test
    void put() {
        UserDto userDto = makeUserDto(1, "user", "user@user.ru");
        userService.saveUser(userDto);
        userDto = makeUserDto(null, "tortiss", null);
        userService.put(1, userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id=:id", User.class);
        User user = query.setParameter("id", 1)
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo("user@user.ru"));
    }

    private UserDto makeUserDto(Integer id, String name, String email) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        return dto;
    }

    @Test
    void getAllUsers() {

        List<UserDto> sourceUsers = List.of(
                makeUserDto(1, "a", "a@email"),
                makeUserDto(2, "b", "b@email"),
                makeUserDto(3, "c", "c@email")
        );
        for (UserDto userDto : sourceUsers) {
            userService.saveUser(userDto);
        }

        // when
        List<UserDto> targetUsers = (List<UserDto>) userService.getAllUsers();

        assertThat(targetUsers, hasSize(sourceUsers.size()));
        for (UserDto sourceUser : sourceUsers) {
            assertThat(targetUsers, hasItem(allOf(hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceUser.getName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }
}