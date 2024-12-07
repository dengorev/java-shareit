package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {
    private Map<Long, User> users;
    private Long id;

    public UserRepository() {
        id = 0L;
        users = new HashMap<>();
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        if (users.values().stream().noneMatch(u -> u.getEmail().equals(user.getEmail()))) {
            if (validation(user)) {
                if (user.getId() == null) {
                    getNextId();
                    user.setId(id);
                }
                users.put(user.getId(), user);
            }
        } else {
            throw new UserAlreadyExistsException("Пользователь с E-mail=" + user.getEmail() + " уже существует!");
        }
        return user;
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Данные введены неверно");
        }
        if (!users.containsKey(user.getId())) {
            throw new UserAlreadyExistsException("Пользователь с ID=" + user.getId() + " не найден");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(users.get(user.getId()).getName());
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(users.get(user.getId()).getEmail());
        }
        if (users.values().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .allMatch(u -> u.getId().equals(user.getId()))) {
            if (validation(user)) {
                users.put(user.getId(), user);
            }
        } else {
            throw new UserAlreadyExistsException("Пользователь с E-mail=" + user.getEmail() + " уже существует!");
        }
        return user;
    }

    public User getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с ID=" + userId + " не найден");
        }
        return users.get(userId);
    }

    public User delete(Long userId) {
        if (userId == null) {
            throw new ValidationException("Данные введены неверно");
        }
        if (!users.containsKey(userId)) {
            throw new UserAlreadyExistsException("Пользователь с ID=" + userId + " не найден");
        }
        return users.remove(userId);
    }

    private boolean validation(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        if ((user.getName().isEmpty())) {
            throw new ValidationException("Некорректный логин пользователя: " + user.getName());
        }
        return true;
    }

    private void getNextId() {
        id++;
    }
}
