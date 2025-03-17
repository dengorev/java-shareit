package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

import static ru.practicum.shareit.booking.constants.Constants.OWNER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDtoResponse create(@Valid @RequestBody ItemDtoRequest itemDto, @RequestHeader(OWNER) Long ownerId) {
        log.info("Создание вещи владельцем с ID={}", ownerId);
        return itemService.create(itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoWithComments getItemById(@PathVariable Long itemId, @RequestHeader(OWNER) Long userId) {
        log.info("Получение вещи с ID={}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoResponse update(@RequestBody ItemDtoRequest itemDto, @PathVariable Long itemId,
                                  @RequestHeader(OWNER) Long ownerId) {
        log.info("Обновление вещи с ID={}", itemId);
        return itemService.update(itemDto, ownerId, itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoWithComments> getItemsByOwner(@RequestHeader(OWNER) Long ownerId) {
        log.info("Получение всех вещей владельца с ID={}", ownerId);
        return itemService.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoResponse> getItemsBySearch(@RequestParam String text) {
        log.info("Поиск вещи с текстом={}", text);
        return itemService.getItemsBySearch(text);
    }

    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoResponse createComment(@RequestHeader(OWNER) Long userId,
                                            @PathVariable Long id, @Valid @RequestBody CommentDtoRequest comment) {
        log.info("Создание комментария");
        return itemService.addComment(userId, id, comment);
    }

}
