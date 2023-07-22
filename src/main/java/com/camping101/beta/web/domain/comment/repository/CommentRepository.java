package com.camping101.beta.web.domain.comment.repository;

import com.camping101.beta.db.entity.camplog.CampLog;
import com.camping101.beta.db.entity.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {

    Page<Comment> findAllByCampLog(CampLog campLog, Pageable pageable);
    //List<Comment> findAllByParentId(Long parentId);

}
