package ru.yandex.practicum.catsgram.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@ToString
public class Image { //модель, которая описывает изображения, прикреплённые к конкретному сообщению.
    Long id; // уникальный идентификатор изображения
    long postId; // уникальный идентификатор поста, к которому прикреплено изображение
    String originalFileName; //имя файла, который содержит изображение
    String filePath; // путь, по которому изображение было сохранено

}
