package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public class TodoRepositoryCustomImpl implements TodoRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Todo> findAllByConditionsWithQueryDsl(String weather, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);  // EntityManager로 JPAQueryFactory 생성

        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        // 조건들 추가
        builder.and(weatherEq(weather));
        builder.and(startDateEq(startDate));
        builder.and(endDateEq(endDate));

        QueryResults<Todo> results = jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset()) // 페이지네이션
                .limit(pageable.getPageSize()) // 페이지네이션
                .fetchResults(); // 전체 개수와 결과 리스트를 함께 가져옴

        // PageImpl을 사용하여 Page 반환
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression weatherEq(String weatherCond) {
        return weatherCond != null && !weatherCond.isEmpty() ? QTodo.todo.weather.eq(weatherCond) : null;
    }

    private BooleanExpression startDateEq(LocalDateTime startDateCond) {
        return startDateCond != null ? QTodo.todo.modifiedAt.goe(startDateCond) : null;
    }

    private BooleanExpression endDateEq(LocalDateTime endDateCond) {
        return endDateCond != null ? QTodo.todo.modifiedAt.loe(endDateCond) : null;
    }
}
