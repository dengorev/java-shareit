package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDtoResponse add(Long id, BookingDtoRequest booking) {
        User booker = userRepository.getUserById(id);
        checkItem(booking.getItemId());
        Item item = itemRepository.getItemById(booking.getItemId());
        Booking newBooking = bookingMapper.modelFromDto(booking, booker, item);
        newBooking.setStatus(BookingStatus.WAITING);
        Booking addBooking = bookingRepository.save(newBooking);
        return bookingMapper.modelToDto(addBooking);
    }

    @Override
    public BookingDtoResponse approved(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.getBookingById(bookingId);
        checkRightApproved(booking, userId);
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingMapper.modelToDto(booking);
    }

    @Override
    public BookingDtoResponse findById(Long userId, Long id) {
        Booking booking = bookingRepository.getBookingById(id);
        checkRight(booking, userId);
        return bookingMapper.modelToDto(booking);
    }

    @Override
    public List<BookingDtoResponse> getAllByBooker(Long userId, String stateParam) {
        BookingState state = BookingState.findBy(stateParam.toUpperCase());
        userRepository.getUserById(userId);
        switch (state) {
            case ALL:
                return bookingRepository.findByBookerIdOrderByStartDesc(userId).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            case WAITING:
                return bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId,
                                LocalDateTime.now()).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId,
                                LocalDateTime.now()).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId,
                                BookingStatus.REJECTED).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), LocalDateTime.now()).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            default:
                throw new ValidationException("Введен неправильный статус");
        }
    }

    @Override
    public List<BookingDtoResponse> getAllByOwner(Long ownerId, String stateParam) {
        BookingState state = BookingState.valueOf(stateParam.toUpperCase());
        userRepository.getUserById(ownerId);
        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId,
                                LocalDateTime.now()).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId,
                                LocalDateTime.now()).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId,
                                BookingStatus.REJECTED).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId,
                                LocalDateTime.now(), LocalDateTime.now()).stream()
                        .map(bookingMapper::modelToDto)
                        .toList();
            default:
                throw new ValidationException("Введен неправильный статус");
        }
    }

    private void checkItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            if (item.get().getAvailable().equals(false)) {
                throw new ValidationException("Вещь недоступна для бронирования");
            }
        } else {
            throw new EntityNotFoundException("Вещь с id " + id + " не существует");
        }
    }

    private void checkRight(Booking booking, Long userId) {
        if (!booking.getBooker().getId().equals(userId) && !itemRepository.findById(booking.getItem().getId())
                .get().getOwnerId().equals(userId)) {
            throw new ValidationException("Вам недоступен просмотр бронирования");
        }
    }

    private void checkRightApproved(Booking booking, Long userId) {
        if (!itemRepository.findById(booking.getItem().getId())
                .get().getOwnerId().equals(userId)) {
            throw new AccessDeniedException("Вам недоступно подтверждение бронирования");
        }
    }
}
