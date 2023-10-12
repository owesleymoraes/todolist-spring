package br.com.wesleymoraes.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "description")
    private String description;

    @Column(length = 50)
    private String title;

    @Column(name = "priority")
    private String priority;

    @Column(name = "startAt")
    private LocalDateTime startAt;

    @Column(name = "endAt")
    private LocalDateTime endAt;

    private UUID idUser;

    @CreationTimestamp
    private LocalDateTime createAt;
}
