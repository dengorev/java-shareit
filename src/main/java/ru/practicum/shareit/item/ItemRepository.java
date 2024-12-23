package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    default Item getItemById(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Вещь не найдена!"));
    }

    List<Item> findAllByOwnerId(Long id);

    @Query("select it from Item as it where it.available = true and " +
            "(LOWER(it.name) like LOWER(?1) or LOWER(it.description) like LOWER(?2))")
    List<Item> findByAvailableTrueAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String text, String text2);
}
