package com.example.project_backend.model.task;

import com.example.project_backend.model.AbstractEntity;
import com.example.project_backend.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.io.Serial;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@FieldNameConstants
public class Task extends AbstractEntity {

    @Serial
    public static final long serialVersionUID = 1L;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}


