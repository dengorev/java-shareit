package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Repository
public class ItemRepository {
    private Map<Long, Item> items;
    private Long id;

    public ItemRepository() {
        id = 0L;
        items = new HashMap<>();
    }

    public Item create(Item item) {
        if (validation(item)) {
            getNextId();
            item.setId(id);
            items.put(item.getId(), item);
        }
        return item;
    }

    public Item getItemById(Long id) {
        if (!items.containsKey(id)) {
            throw new ItemNotFoundException("Вещь с ID=" + id + " не найдена");
        }
        return items.get(id);
    }

    public Item update(Item item) {
        if (item.getId() == null) {
            throw new ValidationException("Некорректный ввод id вещи");
        }
        if (items.containsKey(item.getId())) {
            if (item.getName() == null || item.getName().isBlank()) {
                item.setName(items.get(item.getId()).getName());
            }
            if (item.getDescription() == null || item.getName().isBlank()) {
                item.setDescription(items.get(item.getId()).getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(items.get(item.getId()).getAvailable());
            }
            if (validation(item)) {
                items.put(item.getId(), item);
            }
            return item;
        } else {
            throw new ItemNotFoundException("Вещь с ID=" + item.getId() + " не найдена");
        }
    }

    public List<Item> getItemsByOwner(Long ownerId) {
        return new ArrayList<>(items.values().stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .collect(toList()));
    }

    public List<Item> getItemsBySearch(String text) {
        List<Item> searchItems = new ArrayList<>();
        if (!text.isBlank()) {
            searchItems = items.values().stream()
                    .filter(item -> item.getAvailable())
                    .filter(item -> item.getName().toLowerCase().contains(text) ||
                            item.getDescription().toLowerCase().contains(text))
                    .collect(toList());
        }
        return searchItems;
    }

    private boolean validation(Item item) {
        if ((item.getName().isEmpty()) || (item.getName().isBlank()) || (item.getDescription().isEmpty()) ||
                item.getDescription().isBlank() || (item.getAvailable() == null)) {
            throw new ValidationException("Данные введены неверно");
        }
        return true;
    }

    private void getNextId() {
        id++;
    }
}
