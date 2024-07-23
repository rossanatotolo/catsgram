package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.compare;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    UserService userService;
    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll(int size, SortOrder sort, int from) { //возвращает список созданных постов
        return posts.values()
                .stream()
                .sorted((p0, p1) -> {
                    int result = p0.getPostDate().compareTo(p1.getPostDate());
                    if (sort.equals(SortOrder.DESCENDING)) {
                        result *= -1;
                    }
                    return result;})
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Post create(Post post) { // создаёт новый пост // значение, которое будет передано в метод в качестве аргумента, нужно взять из тела запроса
        if (userService.findUserById(post.getAuthorId()).isEmpty()) {
            throw new ConditionsNotMetException("«Автор с id = " + post.getAuthorId() + " не найден»");
        }

        // проверяем выполнение необходимых условий
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        // формируем дополнительные данные
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        posts.put(post.getId(), post);
        return post;
    }

    private long getNextId() { //Он находит max идентификатор среди уже добавленных публикаций и увеличивает его на единицу.
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Post update(Post newPost) {    //эндпоинт, чтобы редактировать добавленные публикации
        // проверяем необходимые условия
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Optional<Post> findById(long id) {
        if (posts.containsKey(id)) {
            return Optional.of(posts.get(id));
        }
        return Optional.empty();
    }
}
