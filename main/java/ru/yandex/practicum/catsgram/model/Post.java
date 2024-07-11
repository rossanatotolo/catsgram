package ru.yandex.practicum.catsgram.model;

import java.time.Instant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@ToString
public class Post { // модель, описывающая сообщения в социальной сети.
    Long id; // уникальный идентификатор сообщения
    long authorId; // пользователь, который создал сообщение
    String description; //текстовое описание сообщения
    Instant postDate; // дата и время создания сообщения


}
