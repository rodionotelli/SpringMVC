package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.RepositoryInterface;

import java.util.Collection;
import java.util.List;

@Service
public class PostService {
    private final RepositoryInterface repository;

    public PostService(RepositoryInterface repository) {
        this.repository = repository;
    }

    public Collection<Post> all() {
        return repository.all();
    }

    public Post getById(long id) {
        return repository.getById(id).orElseThrow(NotFoundException::new);
    }

    public Post save(Post post) {
        return repository.save(post);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}

