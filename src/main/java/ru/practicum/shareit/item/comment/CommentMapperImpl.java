package ru.practicum.shareit.item.comment;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.comment.Comment.CommentBuilder;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentDto toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    @Override
    public Comment toComment(CommentDto commentDto, User user, Item item) {
        if (commentDto == null) {
            return null;
        }
        CommentBuilder comment = Comment.builder();
        comment.id(commentDto.getId());
        comment.text(commentDto.getText());
        comment.item(item);
        comment.author(user);
        comment.created(LocalDateTime.now());
        return comment.build();
    }
}
