package com.example.demo.entities;

import com.example.demo.entities.common.SaveAuto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Category extends SaveAuto {

    @Column(nullable = false, length = 100)
    private String categoryTitle;

    private String categoryDescription;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Post> posts=new ArrayList<>();
}
