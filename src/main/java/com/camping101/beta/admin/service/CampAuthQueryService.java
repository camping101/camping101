package com.camping101.beta.admin.service;

import static com.camping101.beta.admin.entity.QCampAuth.campAuth;
import static com.camping101.beta.camp.entity.QCamp.camp;

import com.camping101.beta.admin.dto.CampAuthListSearchResponse;
import com.camping101.beta.admin.dto.CampAuthSearchRequest;
import com.camping101.beta.admin.dto.QCampAuthListSearchResponse;
import com.camping101.beta.admin.entity.CampAuth;
import com.camping101.beta.admin.status.CampAuthStatus;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CampAuthQueryService {

    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public CampAuthQueryService(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);

    }

//
//    public CampAuthListSearchResponse findCampAuthList(CampAuthSearchRequest campAuthSearchRequest,
//        Pageable pageable) {
//
////        private Long campId;
////        private String campName;
////        private String location;
////        private String tel;
////        private String email;
////        private LocalDateTime createDate;
////        private String campAuthStatus;
//
////        return queryFactory.select(new QCampAuthListSearchResponse(
////                camp.campId,
////                camp.name,
////                camp.tel,
////                camp.member.email,
////                campAuth.createdAt,
////                String.valueOf(campAuth.campAuthStatus)))
////            .from(campAuth)
////            .innerJoin(campAuth.camp, camp)
////            .where(campNameEq(campAuthSearchRequest.getCampName()),
////                campAuthStatusEq(campAuthSearchRequest.getCampAuthStatus()),
////                nickNameEq(campAuthSearchRequest.getNickName()))
////            .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
////            .fetchResults();
//
//    }

    private BooleanExpression campNameEq(String campName) {

        if (campName != null) {
            return campAuth.camp.name.eq(campName);
        }

        return null;

    }

//    private BooleanExpression locationEq(String location) {
//
//        if (location != null) {
//            return campAuth.camp.location.eq(location);
//        }
//
//        return null;
//
//    }

    private BooleanExpression nickNameEq(String nickName) {

        if (nickName != null) {
            return camp.member.nickName.eq(nickName);
        }

        return null;
    }

    private BooleanExpression campAuthStatusEq(String campAuthStatus) {

        if (campAuthStatus != null) {

            return campAuth.campAuthStatus.eq(CampAuthStatus.valueOf(campAuthStatus));

        }

        return null;


    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {

        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {

            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(CampAuth.class, "createdAt");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));

        });

        return orders;

    }
}
