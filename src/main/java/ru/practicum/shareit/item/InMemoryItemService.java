package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class InMemoryItemService implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Override
    public ItemDto create(ItemDto itemDto, Long ownerId) {
        ItemDto newItemDto = null;
        if (isExistUser(ownerId)) {
            newItemDto = itemMapper.toItemDto(itemStorage.create(itemMapper.toItem(itemDto, ownerId)));
        }
        return newItemDto;
    }

    @Override
    public ItemDto getItemById(Long id) {
        return itemMapper.toItemDto(itemStorage.getItemById(id));
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long ownerId, Long itemId) {
        ItemDto newItemDto = null;
        if (isExistUser(ownerId)) {
            if (itemDto.getId() == null) {
                itemDto.setId(itemId);
            }
            Item oldItem = itemStorage.getItemById(itemId);
            if (!oldItem.getOwnerId().equals(ownerId)) {
                throw new ItemNotFoundException("У владельца нет такой вещи");
            }
            newItemDto = itemMapper.toItemDto(itemStorage.update(itemMapper.toItem(itemDto, ownerId)));
        }
        return newItemDto;
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        return itemStorage.getItemsByOwner(ownerId).stream()
                .map(itemMapper::toItemDto)
                .collect(toList());
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        text = text.toLowerCase();
        return itemStorage.getItemsBySearch(text).stream()
                .map(itemMapper::toItemDto)
                .collect(toList());
    }

    public boolean isExistUser(Long userId) {
        boolean exist = false;
        if (userService.getUserById(userId) != null) {
            exist = true;
        }
        return exist;
    }
}
