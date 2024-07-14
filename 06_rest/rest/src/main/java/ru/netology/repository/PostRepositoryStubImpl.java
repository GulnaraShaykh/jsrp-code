package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
  private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
  private final AtomicLong counter = new AtomicLong();

  @Override
  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  @Override
  public Optional<Post> getById(long id) {
    return Optional.ofNullable(posts.get(id));
  }

  @Override
  public Post save(Post post) {
    if (post.getId() == 0) {
      long id = counter.incrementAndGet();
      post.setId(id);
      posts.put(id, post);
    } else {
      posts.put(post.getId(), post);
    }
    return post;
  }

  @Override
  public void removeById(long id) {
    posts.remove(id);
  }
}