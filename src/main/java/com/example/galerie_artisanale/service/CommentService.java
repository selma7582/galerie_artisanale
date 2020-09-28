package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Comment;

public interface CommentService {

    Comment save(Comment comment);

    void removeOne(Long id) ;


}
