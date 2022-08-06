package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDto.UserDtoBuilder;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDtoBuilder userDto = UserDto.builder();
        userDto.id(user.getId());
        userDto.name(user.getName());
        userDto.email(user.getEmail());
        return userDto.build();
    }

    @Override
    public User toUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        String email = null;
        email = userDto.getEmail();
        User user = new User(email);
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        return user;
    }

    @Override
    public void updateUserFromUserDto(UserDto userDto, User user) {
        if (userDto == null) {
            return;
        }
        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
    }
}
