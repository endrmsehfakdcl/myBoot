package com.keduit.repository;

import com.keduit.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

    List<TodoEntity> findByUserId(String userId);

    @Query("SELECT t from TodoEntity t WHERE t.userId = ?1")
    TodoEntity findByUserIdQuery(String userId);
}
