package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingBookerDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDbRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemDbRepository itemRepository;
    private ItemMapper itemMapper;
    private UserDbRepository userRepository;
    private BookingRepository bookingRepository;
    private BookingMapper bookingMapper;
    private CommentMapper commentMapper;
    private CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemDbRepository itemRepository, ItemMapper itemMapper,
                           UserDbRepository userRepository, BookingRepository bookingRepository,
                           BookingMapper bookingMapper, CommentMapper commentMapper,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public Optional<ItemDto> get(Integer userId, Integer itemId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!itemRepository.findById(itemId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findById(userId).get();
        Item item = itemRepository.findById(itemId).get();
        Booking lastBooking = bookingRepository.findFirstByItemOwnerDesc(itemId, user, LocalDateTime.now());
        BookingBookerDto last = bookingMapper.toBookingBookerDto(lastBooking);
        Booking firstBooking = bookingRepository.findFirstByItemOwnerAsc(itemId, user, LocalDateTime.now());
        BookingBookerDto next = bookingMapper.toBookingBookerDto(firstBooking);
        List<CommentDto> commentDtoList;
        List<Comment> commentList = commentRepository.findAllByItemOrderByCreated(item);
        commentDtoList = commentList.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());

        return Optional.of(itemMapper.toItemFullDto(item, last, next, commentDtoList));
    }

    @Override
    public ItemDto addNewItem(Integer userId, ItemDto itemDto) {
        if (userRepository.findAll().contains(userRepository.findById(userId).get())) {
            itemDto.setOwner(userRepository.findById(userId).get());
            Item item = itemMapper.toItem(itemDto);
            return itemMapper.toItemDto(itemRepository.save(item));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteItem(Integer userId, Integer itemId) {
        if (itemRepository.findById(itemId).get().getOwner().getId().equals(userId)) {
            itemRepository.deleteById(itemId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ItemDto update(Integer userId, Integer itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId).get();
        if (item.getOwner().getId().equals(userId)) {
            itemMapper.updateItemFromItemDto(itemDto, item);
            itemRepository.save(item);
            item = itemRepository.findById(itemId).get();
            return itemMapper.toItemDto(item);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<ItemDto> getItems(Integer userId) {
        Collection<ItemDto> itemDtoCollection = new ArrayList<>();
        Collection<Item> itemCollection = itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .sorted(Comparator.comparing(Item::getId))
                .collect(Collectors.toList());
        for (Item item : itemCollection) {
            itemDtoCollection.add(getItemBookings(item.getId()).get());
        }
        return itemDtoCollection;
    }


    private Optional<ItemDto> getItemBookings(Integer itemId) {
        if (!itemRepository.findById(itemId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Item item = itemRepository.findById(itemId).get();
        Booking lastBooking =
                bookingRepository.findFirstByItemDesc(item,
                        LocalDateTime.now(), Status.APPROVED);
        BookingBookerDto last = bookingMapper.toBookingBookerDto(lastBooking);
        Booking firstBooking =
                bookingRepository.findFirstByItemAsc(item,
                        LocalDateTime.now(), Status.APPROVED);
        BookingBookerDto next = bookingMapper.toBookingBookerDto(firstBooking);
        List<CommentDto> commentDtoList = new ArrayList<>();
        if (commentRepository.findAllByItemOrderByCreated(item).isEmpty()) {
            List<Comment> commentList = commentRepository.findAllByItemOrderByCreated(item);
            commentDtoList = commentList.stream()
                    .map(commentMapper::toCommentDto)
                    .collect(Collectors.toList());
        }
        return Optional.of(itemMapper.toItemFullDto(item, last, next, commentDtoList));
    }

    @Override
    public Collection<ItemDto> search(Integer userId, String text) {
        if (text.length() == 0) {
            return new ArrayList<>();
        }
        Collection<ItemDto> itemDtoCollection = new ArrayList<>();
        Collection<Item> itemCollection = new ArrayList<>(itemRepository.search(text));
        for (Item item : itemCollection) {
            itemDtoCollection.add(itemMapper.toItemDto(item));
        }
        return itemDtoCollection;
    }

    @Override
    public CommentDto createComment(Integer userId, Integer itemId, CommentDto commentDto) {
        if (!itemRepository.findById(itemId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!userRepository.findById(userId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (commentDto.getText().length() == 0 || commentDto.getText() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findById(userId).get();
        Item item = itemRepository.findById(itemId).get();
        if (bookingRepository.findFirstByItemAndBookerDesc(item, user, LocalDateTime.now(), Status.APPROVED) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Comment comment = commentMapper.toComment(commentDto, user, item);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }
}
