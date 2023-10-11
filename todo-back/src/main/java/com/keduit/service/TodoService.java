package com.keduit.service;

import com.keduit.model.TodoEntity;
import com.keduit.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {
    private final TodoRepository repository;

    public String testService(){
        TodoEntity todoEntity = TodoEntity.builder().title("My first todo List").build();
        repository.save(todoEntity);
        TodoEntity savedEntity = repository.findById(todoEntity.getId()).get();

        return savedEntity.getTitle();
    }

    private void validate(final TodoEntity todoEntity){
        if(todoEntity == null){
            log.warn("Entity is null");
            throw new RuntimeException("Entity is null");
        }

        if(todoEntity.getUserId() == null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
    public List<TodoEntity> create(TodoEntity todoEntity){
        validate(todoEntity);
        repository.save(todoEntity);
        log.info("Entity Id: {} is saved.", todoEntity.getId());

        return repository.findByUserId(todoEntity.getUserId());
    }

    public List<TodoEntity> read(String userId){
        return repository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity todoEntity){
        validate(todoEntity);
        final Optional<TodoEntity> original = repository.findById(todoEntity.getId());
        original.ifPresent(todoEntity1 -> {
            todoEntity1.setTitle(todoEntity.getTitle());
            todoEntity1.setDone(todoEntity1.isDone());
            repository.save(todoEntity1);
        });

        return read(todoEntity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity todoEntity) {
        validate(todoEntity);

        try{
            repository.delete(todoEntity);
        } catch (Exception e) {
            log.error("delete error : " + todoEntity.getId(), e);
            throw new RuntimeException("delete error" + todoEntity.getId());
        }

        return read(todoEntity.getUserId());
    }

}
