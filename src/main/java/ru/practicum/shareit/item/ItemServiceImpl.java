package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public ItemDtoResponse create(ItemDtoRequest itemDto, Long ownerId) {
        userRepository.getUserById(ownerId);
        Item item = itemMapper.modelFromDto(itemDto);
        item.setOwnerId(ownerId);
        Item saveItem = itemRepository.save(item);
        return itemMapper.modelToDtoResponse(saveItem);
    }

    @Override
    public ItemDtoWithComments getItemById(Long id, Long userId) {
        Item item = itemRepository.getItemById(id);
        ItemDtoWithComments itemDto = itemMapper.mapToDtoWithComments(item);
        if (userId == item.getOwnerId()) {
            List<Booking> bookings = bookingRepository.findAllByItemIdAndEndBeforeOrderByStartDesc(id,
                    LocalDateTime.now());
            if (!bookings.isEmpty()) {
                Booking booking = bookings.getFirst();
                itemDto.setLastBooking(booking.getStart());
            }
            bookings = bookingRepository.findAllByItemIdAndStartAfterOrderByStartDesc(id, LocalDateTime.now());
            if (!bookings.isEmpty()) {
                Booking nextBooking = bookings.getFirst();
                itemDto.setNextBooking(nextBooking.getStart());
            }
        }
        itemDto.setComments(commentRepository.findByItemId(id).stream()
                .map(commentMapper::modelToDtoResponse)
                .toList());
        return itemDto;
    }

    @Override
    public ItemDtoResponse update(ItemDtoRequest itemDto, Long ownerId, Long itemId) {
        Item oldItem = itemRepository.getItemById(itemId);
        checkOwner(ownerId, itemId);
        oldItem.setName(Optional.ofNullable(itemDto.getName()).filter(name
                -> !name.isBlank()).orElse(oldItem.getName()));
        oldItem.setDescription(Optional.ofNullable(itemDto.getDescription()).filter(description
                -> !description.isBlank()).orElse(oldItem.getDescription()));
        oldItem.setAvailable(Optional.ofNullable(itemDto.getAvailable()).filter(available
                -> available.describeConstable().isPresent()).orElse(oldItem.getAvailable()));
        return itemMapper.modelToDtoResponse(itemRepository.save(oldItem));
    }

    @Override
    public List<ItemDtoWithComments> getItemsByOwner(Long ownerId) {
        Map<Long, Item> itemMap = itemRepository.findAllByOwnerId(ownerId)
                .stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));
        Map<Long, List<Comment>> commentMap = commentRepository.findAllByItemIdIn((itemMap.keySet()))
                .stream()
                .collect(Collectors.groupingBy(Comment::getItemId));
        return itemMap.values()
                .stream()
                .map(item -> makePostWithCommentsDto(item,
                        commentMap.getOrDefault(item.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoResponse> getItemsBySearch(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemRepository
                    .findByAvailableTrueAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text)
                    .stream()
                    .map(itemMapper::modelToDtoResponse).toList();
        }
    }

    @Override
    public CommentDtoResponse addComment(Long userId, Long itemId, CommentDtoRequest comment) {
        User user = userRepository.getUserById(userId);
        itemRepository.getItemById(itemId);
        if (!bookingRepository
                .existsAllByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())) {
            throw new ValidationException("Вы не можете оставить комментарий");
        }
        Comment addComment = commentMapper.modelFromDto(comment);
        addComment.setAuthor(user);
        addComment.setItemId(itemId);
        addComment.setCreated(LocalDateTime.now());
        return commentMapper.modelToDtoResponse(commentRepository.save(addComment));
    }

    private void checkOwner(Long ownerId, Long itemId) {
        if (ownerId != itemRepository.getItemById(itemId).getOwnerId()) {
            throw new EntityNotFoundException("Вещь не принадлежит пользователю с id " + ownerId);
        }
    }

    private ItemDtoWithComments makePostWithCommentsDto(Item item, List<Comment> comments) {
        List<CommentDtoResponse> commentsDto = comments
                .stream()
                .map(commentMapper::modelToDtoResponse)
                .collect(Collectors.toList());
        ItemDtoWithComments itemDtoWithComments = itemMapper.mapToDtoWithComments(item);
        itemDtoWithComments.setComments(commentsDto);
        return itemDtoWithComments;
    }
}
