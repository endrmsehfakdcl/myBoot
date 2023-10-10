package com.keduit.dto;

import com.keduit.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TodoDTO {
    private String id;
    private String title;
    private boolean done;

    public TodoDTO (final TodoEntity entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
    }

    public static TodoEntity todoEntity(TodoDTO todoDTO){
        return TodoEntity.builder().title(todoDTO.title).id(todoDTO.getId()).done(todoDTO.isDone()).build();
    }


}
