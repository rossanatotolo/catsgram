package ru.yandex.practicum.catsgram.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping //  GET /users — для получения списка пользователей.
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @PostMapping() // для добавления нового пользователя в список.
    @ResponseStatus(HttpStatus.CREATED)
    public User userCreate(@RequestBody User user) { // значение, которое будет передано в метод в качестве аргумента, нужно взять из тела запроса
        return userService.userCreate(user);
    }

    @PutMapping() //для обновления данных существующего пользователя.
    public User userUpdate(@RequestBody User newUser) {
        return userService.userUpdate(newUser);
    }

    @GetMapping("/user/{userId}")
    public Optional<User> findUserById(@PathVariable("userId") long id) {
        return userService.findUserById(id);
    }

}