package ru.yandex.practicum.catsgram.model;

import java.time.Instant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = {"email"})
@Getter
@Setter
@ToString
public class User { //модель, описывающая пользователей социальной сети.
    Long id; //уникальный идентификатор пользователя
    String username; //имя пользователя
    String email; // электронная почта пользователя
    String password; //пароль пользователя
    Instant registrationDate;   //дата и время регистрации.

}
