package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gift_certificates")
@Data
@NoArgsConstructor
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "Gift certificate name should be between 2 and 50 characters")
    @NotBlank(message = "Gift certificate name should contain not only blank characters")
    private String name;

    @Column(nullable = false)
    @Size(min = 1, max = 255, message = "Description should be between 1 and 255 characters")
    @NotBlank(message = "Description should contain not only blank characters")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Price should not be null")
    @Positive(message = "Price should be positive")
    @DecimalMax(value = "10000", message = "Price should be less than 10000")
    private BigDecimal price;

    @Column(nullable = false)
    @NotNull(message = "Certificate duration should not be null")
    @Min(value = 1, message = "Certificate duration should not be less than 1")
    @Max(value = 365, message = "Certificate duration should be less than 365 days")
    private Integer duration;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @ManyToMany
    @JoinTable(name = "tags_certificates"
            , joinColumns = @JoinColumn(name = "gift_certificate_id", referencedColumnName = "id")
            , inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<@Valid Tag> tags = new HashSet<>();

    public boolean addTag(Tag tag) {
        return tags.add(tag);
    }
}
