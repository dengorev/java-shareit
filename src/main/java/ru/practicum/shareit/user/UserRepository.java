package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    default User getUserById(long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    List<User> findByEmailContainingIgnoreCase(String emailSearch);
}
