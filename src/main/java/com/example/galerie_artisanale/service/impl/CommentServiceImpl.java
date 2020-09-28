package com.example.galerie_artisanale.service.impl;

import com.example.galerie_artisanale.entity.Comment;
import com.example.galerie_artisanale.repository.CommentRepository;
import com.example.galerie_artisanale.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {


    @Autowired
    private CommentRepository commentRepository;


    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void removeOne(Long id) {
        commentRepository.deleteById(id);
    }


}
