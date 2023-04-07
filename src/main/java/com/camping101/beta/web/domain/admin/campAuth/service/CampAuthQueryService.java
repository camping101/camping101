//package com.camping101.beta.admin.campAuth.service;
//
//import com.camping101.beta.admin.campAuth.entity.CampAuth;
//import com.querydsl.core.types.Order;
//import com.querydsl.core.types.OrderSpecifier;
//import com.querydsl.core.types.dsl.PathBuilder;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import javax.persistence.EntityManager;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@Transactional
//public class CampAuthQueryService {
//
//    private EntityManager em;
//    private JPAQueryFactory queryFactory;
//
//    public CampAuthQueryService(EntityManager em) {
//        this.em = em;
//        this.queryFactory = new JPAQueryFactory(em);
//
//    }
//
//
////    public List<CampAuthListSearchResponse> findCampAuthList(CampAuthSearchRequest campAuthSearchRequest,
////        Pageable pageable) {
////
////        return queryFactory.select(new QCampAuthListSearchResponse(
////                camp.campId,
////                camp.name,
////                camp.location,
////                camp.tel,
////                camp.member.email,
////                campAuth.createdAt,
////                campAuth.campAuthStatus))
////            .from(campAuth)
////            .innerJoin(campAuth.camp, camp)
////            .where(campNameEq(campAuthSearchRequest.getCampName()),
////                campAuthStatusEq(campAuthSearchRequest.getCampAuthStatus()),
////                nickNameEq(campAuthSearchRequest.getNickName()))
////            .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
////            .fetch();
////
////    }
////
////    private BooleanExpression campNameEq(String campName) {
////
////        if (campName != null) {
////            return campAuth.camp.name.eq(campName);
////        }
////
////        return null;
////
////    }
////
////    private BooleanExpression nickNameEq(String nickName) {
////
////        if (nickName != null) {
////            return camp.member.nickname.eq(nickName);
////        }
////
////        return null;
////    }
////
////    private BooleanExpression campAuthStatusEq(String campAuthStatus) {
////
////        if (campAuthStatus != null) {
////
////            return campAuth.campAuthStatus.eq(CampAuthStatus.valueOf(campAuthStatus));
////
////        }
////
////        return null;
////
////    }
//
//    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
//
//        List<OrderSpecifier> orders = new ArrayList<>();
//
//        sort.stream().forEach(order -> {
//
//            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
//
//            String prop = order.getProperty();
//
//            PathBuilder orderByExpression = new PathBuilder(CampAuth.class, "createdAt");
//            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
//
//        });
//
//        return orders;
//
//    }
//}
