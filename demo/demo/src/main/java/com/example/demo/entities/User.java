package com.example.demo.entities;

import com.example.demo.entities.common.SaveAuto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class User extends SaveAuto {

    @Column(nullable = false)
    private String name;
    private String email;
    private String password;
    private String about;

}
