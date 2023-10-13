package com.keduit.controller;

import com.keduit.dto.ResponseDTO;
import com.keduit.dto.TodoDTO;
import com.keduit.model.TodoEntity;
import com.keduit.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo(){
        String str = todoService.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder().data(list).build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDTO,
                                        @AuthenticationPrincipal String userId){
        try {
            TodoEntity todoEntity = TodoDTO.todoEntity(todoDTO);
            todoEntity.setId(null);
            todoEntity.setUserId(userId);

            List<TodoEntity> entities = todoService.create(todoEntity);
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO todoDTO,
                                        @AuthenticationPrincipal String userId) {
        TodoEntity todoEntity = TodoDTO.todoEntity(todoDTO);
        todoEntity.setUserId(userId);

        List<TodoEntity> entities = todoService.update(todoEntity);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO todoDTO,
                                        @AuthenticationPrincipal String userId){
        try{
            TodoEntity todoEntity = TodoDTO.todoEntity(todoDTO);
            todoEntity.setUserId(userId);

            List<TodoEntity> entities = todoService.delete(todoEntity);
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping
    public ResponseEntity<?> selectTodo(@AuthenticationPrincipal String userId){
        try{
            List<TodoEntity> entities = todoService.read(userId);
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
