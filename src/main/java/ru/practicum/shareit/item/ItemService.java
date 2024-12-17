package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long ownerId);

    ItemDto getItemById(Long id);

    ItemDto update(ItemDto itemDto, Long itemId, Long ownerId);

    List<ItemDto> getItemsByOwner(Long ownerId);

    List<ItemDto> getItemsBySearch(String text);
}
