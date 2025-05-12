package org.example.expert.domain.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.example.expert.domain.todo.entity.Todo;

import java.time.LocalDateTime;

public interface TodoRepositoryCustom {
    Page<Todo> findAllByConditionsWithQueryDsl(
            @Param("weather") String weather,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
