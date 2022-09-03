package ru.practicum.shareit.item.comment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Rollback(false)
@SpringBootTest(
        properties = "db.name=test7",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentMapperImplTest {
    @Autowired
    private CommentMapper commentMapper;
    private Item item;
    private User user;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = new User(1, "testName2", "test2@email.ru");
        itemDto = new ItemDto(null, "name", "description", true,
                null, null, null, null,
                null, new ArrayList<>());
        item = new Item(1, "name", "test", true, user, null);
        commentDto = new CommentDto(null, "trololo", null, null);
        comment = new Comment(1, "text", item, user, LocalDateTime.now());
    }

    @Test
    void toCommentDto() {
        commentDto = commentMapper.toCommentDto(comment);
        assertEquals(commentDto.getText(), comment.getText());
        assertEquals(commentDto.getAuthorName(), comment.getAuthor().getName());
    }

    @Test
    void toComment() {
        comment = commentMapper.toComment(commentDto, user, item);
        assertEquals(comment.getAuthor(), user);
        assertEquals(comment.getItem(), item);
    }
}