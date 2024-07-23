package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getAllUsers() { //    GET /users — для получения списка пользователей.
        return users.values();
    }

    public User userCreate(User user) { // для добавления нового пользователя в список. // значение, которое будет передано в метод в качестве аргумента, нужно взять из тела запроса
        // проверяем выполнение необходимых условий
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (users.containsValue(user)) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        // формируем дополнительные данные
        user.setId(getIdNext());
        user.setRegistrationDate(Instant.now());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    private long getIdNext() { //Он находит max идентификатор среди уже добавленных публикаций и увеличивает его на единицу.
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public User userUpdate(User newUser) { //для обновления данных существующего пользователя.
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        for (User user : users.values()) { //проверяем мапу, используется ли у кого-то имейл
            if (user.getEmail().equals(newUser.getEmail())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() != null) {
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getUsername() != null) {
                oldUser.setUsername(newUser.getUsername());
            }
            if (newUser.getPassword() != null) {
                oldUser.setPassword(newUser.getPassword());
            }
            return oldUser;
        }
        throw new NotFoundException("User с id = " + newUser.getId() + " не найден");
    }

    public Optional<User> findUserById(long id) { // находит (и возвращает) пользователя по id
        if(users.containsKey(id)) {
            return Optional.of(users.get(id));
        } else {
            return Optional.empty();
        }
    }

}
