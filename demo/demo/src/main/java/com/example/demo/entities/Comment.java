package com.example.demo.entities;

import com.example.demo.entities.common.SaveAuto;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Comment extends SaveAuto {


    private String content;

}
