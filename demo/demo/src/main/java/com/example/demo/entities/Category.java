package com.example.demo.entities;

import com.example.demo.entities.common.SaveAuto;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Category extends SaveAuto {

    private String categoryTitle;
    private String categoryDescription;

}
