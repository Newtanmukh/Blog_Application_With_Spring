package com.example.demo.entities.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.*;
import org.hibernate.boot.jaxb.mapping.GenerationTiming;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class SaveAuto extends CreatedAtAuto{

    @Generated(event = EventType.UPDATE)
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

}
