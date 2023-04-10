package com.camping101.beta.web.domain.member.repository;

import com.camping101.beta.db.entity.member.AccessTokenBlackList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenBlackListRepository extends CrudRepository<AccessTokenBlackList, Long> {

}
