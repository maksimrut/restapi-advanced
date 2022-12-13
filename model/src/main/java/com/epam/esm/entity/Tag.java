package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    @Size(min = 2, max = 20, message = "Tag name should be between 2 and 20 characters")
    @NotBlank(message = "Tag name should contain not only blank characters")
    private String name;
}
