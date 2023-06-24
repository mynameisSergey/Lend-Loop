package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
@TestPropertySource(properties = {"db.name=comment_test"})
public class CommentMapperTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    @Order(1)
    void toComment() {
        User user = new User(1, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        Item item = new Item(1, "itemName", "item description", true, user);
        itemRepository.save(item);
        CommentDto commentDto = new CommentDto(1, "text", LocalDate.now(), 1, "Karl");
        Assertions.assertEquals(CommentMapper.toComment(commentDto, item, user).getId(),
                1);
    }

    @Test
    @Order(2)
    void toCommentDto() {
        User user = new User(2, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        Item item = new Item(2, "itemName", "item description", true, user);
        itemRepository.save(item);
        Comment comment = new Comment(2, "text", item, user, LocalDate.now());
        Assertions.assertEquals(CommentMapper.toCommentDto(comment).getId(),
                2);
    }
}