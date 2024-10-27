package ru.netology.repository;


import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository implements RepositoryInterface {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public List<Post> all() {
        var visiblePosts = new ArrayList<Post>();
        for (Post post : posts.values())
            if (!post.getRemoved()) {
                visiblePosts.add(post);
            }
        return visiblePosts;
    }

    public Optional<Post> getById(long id) {
        for (Post post : posts.values())
            if (post.getId() == id && post.getRemoved()) {
                throw new NotFoundException();
            }
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        for (Post postRemoved : posts.values())
            if (postRemoved.getRemoved()) {
                throw new NotFoundException();
            }
        if (post.getId() == 0) {
            long id = idCounter.incrementAndGet();

            while (posts.containsKey(id))
                id = idCounter.incrementAndGet();

            post.setId(id);
            posts.put(id,post);
        } else if (post.getId() != 0) {
            Long currentId = post.getId();
            posts.put(currentId, post);
        }
        return post;
    }

    public void removeById(long id) {
        if (!posts.containsKey(id)) {
            throw new NotFoundException();
        }
        for (Post post : posts.values()) {
            if (post.getId() == id) {
                post.setRemoved(true);
            }
        }
    }
}