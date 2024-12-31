package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDtoResponse create(ItemDtoRequest item, Long ownerId);

    ItemDtoWithComments getItemById(Long id, Long userId);

    ItemDtoResponse update(ItemDtoRequest itemDto, Long itemId, Long ownerId);

    List<ItemDtoWithComments> getItemsByOwner(Long ownerId);

    List<ItemDtoResponse> getItemsBySearch(String text);

    CommentDtoResponse addComment(Long userId, Long itemId, CommentDtoRequest comment);
}
