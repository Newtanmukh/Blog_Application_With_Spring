package com.example.demo.repositories;

import com.example.demo.entities.Category;
import com.example.demo.entities.Post;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);

    List<Post> findByTitle(String title);
}
