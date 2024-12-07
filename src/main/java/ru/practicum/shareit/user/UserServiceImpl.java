package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userStorage;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(userMapper::toUserDto)
                .collect(toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toUserDto(userStorage.getUserById(id));
    }

    @Override
    public UserDto create(UserDto userDto) {
        return userMapper.toUserDto(userStorage.create(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        if (userDto.getId() == null) {
            userDto.setId(id);
        }
        return userMapper.toUserDto(userStorage.update(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto delete(Long userId) {
        return userMapper.toUserDto(userStorage.delete(userId));
    }
}
