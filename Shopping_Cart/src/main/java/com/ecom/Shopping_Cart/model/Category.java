package com.ecom.Shopping_Cart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

//@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String ImageName;

    private Boolean isActive;

}
