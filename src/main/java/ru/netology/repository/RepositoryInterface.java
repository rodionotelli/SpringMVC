package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Collection;
import java.util.Optional;

public interface RepositoryInterface {

    Collection<Post> all();

    Optional<Post> getById(long id);

    Post save(Post post);

    void removeById(long id);

}
