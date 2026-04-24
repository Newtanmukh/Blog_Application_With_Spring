package com.example.demo.entities;

import com.example.demo.entities.common.SaveAuto;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "comment")
public class Comment extends SaveAuto {


    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
