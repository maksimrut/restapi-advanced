package com.epam.esm.service.dto;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderDto extends RepresentationModel<OrderDto> {

    private Long id;
    private BigDecimal orderCost;
    private LocalDateTime orderDate;
    private User user;
    private List<GiftCertificateDto> certificateListDto = new ArrayList<>();
}
