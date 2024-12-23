package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictDataException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toUserDto(userRepository.getUserById(id));
    }

    @Override
    public UserDto create(UserDto userDto) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        userDto.setId(id);
        checkEmail(userDto);
        User oldUser = userRepository.getUserById(id);
        oldUser.setEmail(Optional.ofNullable(userDto.getEmail()).filter(email
                -> !email.isBlank()).orElse(oldUser.getEmail()));
        oldUser.setName(Optional.ofNullable(userDto.getName()).filter(name
                -> !name.isBlank()).orElse(oldUser.getName()));
        return userMapper.toUserDto(userRepository.save(oldUser));
    }

    @Override
    public void delete(Long userId) {
        userRepository.getUserById(userId);
        userRepository.deleteById(userId);
    }

    private void checkEmail(UserDto user) {
        List<User> users = userRepository.findByEmailContainingIgnoreCase(user.getEmail());
        if (!users.isEmpty()) {
            if (user.getEmail().equals(users.getFirst().getEmail()) && !Objects.equals(user.getId(), users.getFirst().getId())) {
                throw new ConflictDataException("Такой email уже используется");
            }
        }
    }
}
