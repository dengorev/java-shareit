package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.ValidationException;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static BookingState findBy(String name) {
        for (BookingState names : BookingState.values()) {
            if (name.equalsIgnoreCase(names.name())) {
                return names;
            }
        }
        throw new ValidationException("Неправильно указан статус");
    }
}
