package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
