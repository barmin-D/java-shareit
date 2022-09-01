package ru.practicum.shareit.user.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Rollback(false)
@SpringBootTest(
        properties = "db.name=test7",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserMapperImplTest {
    @Autowired
    UserMapper userMapper;
    private User user;
    private UserDto userDto;

    @Test
    void toUserDto() {
        user = new User(1, "testName2", "test2@email.ru");
        userDto = userMapper.toUserDto(user);
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void toUser() {
        userDto = new UserDto(1, "testName", "test@email.ru");
        user = userMapper.toUser(userDto);
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void updateUserFromUserDto() {
        userDto = new UserDto(1, "n", "test@email.ru");
        user = new User(1, "testName2", "test2@email.ru");
        userMapper.updateUserFromUserDto(userDto, user);
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }
}