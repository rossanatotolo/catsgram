package ru.yandex.practicum.catsgram.controller;

import java.util.Collection;
import java.util.Optional;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;
import ru.yandex.practicum.catsgram.service.PostService;


@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam(defaultValue = "10") @Positive Integer size, @RequestParam(defaultValue = "desc") String sort, @RequestParam(defaultValue = "0") @Min(value = 0) Integer from) throws ParameterNotValidException {
        if (size <= 0) {
            throw new ParameterNotValidException(size.toString(), "Некорректный размер выборки. Размер должен быть больше нуля");
        }
        if (sort == null) {
            throw new ParameterNotValidException(sort, "Получено: " + sort + " должно быть: ask или desc");
        }
        if (from < 0) {
            throw new ParameterNotValidException(from.toString(), "Начало выборки должно быть положительным числом");
        }
        return postService.findAll(from, SortOrder.from(sort), from);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }

    @GetMapping("/post/{postId}")
    public Optional<Post> findById(@PathVariable("postId") long id) {
        return postService.findById(id);
    }

}