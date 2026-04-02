package com.example.demo.entities.common;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@MappedSuperclass
public class IdAuto implements Serializable {

    @Serial
    private static final long serialVersionUID = 3208734567898621L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
