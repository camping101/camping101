package com.camping101.beta.comment.repository;

import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.comment.entity.Comment;
import com.camping101.beta.member.entity.Member;
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
