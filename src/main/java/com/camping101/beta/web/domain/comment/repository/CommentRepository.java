package com.camping101.beta.web.domain.comment.repository;

import com.camping101.beta.db.entity.campLog.CampLog;
import com.camping101.beta.db.entity.comment.Comment;
import com.camping101.beta.db.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByMemberAndCampLog(Member member, CampLog campLog);
    Page<Comment> findAllByCampLog(CampLog campLog, Pageable pageable);
    List<Comment> findAllByParentId(Long parentId);
    long countAllByCampLog(CampLog campLog);

}
