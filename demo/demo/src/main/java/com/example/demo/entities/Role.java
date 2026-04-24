package com.example.demo.entities;

import com.example.demo.entities.common.IdAuto;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Role extends IdAuto {

    private String name;
}