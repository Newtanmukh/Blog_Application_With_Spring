package com.example.demo.entities;

import com.example.demo.entities.common.SaveAuto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Post extends SaveAuto {

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 10000)
    private String content;

    private String imageName;

    private LocalDate addDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
