package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public interface CommentMapper {
    CommentDto toCommentDto(Comment comment);

    Comment toComment(CommentDto commentDto, User user, Item item);
}
