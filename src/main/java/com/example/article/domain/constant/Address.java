package com.example.article.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Column(columnDefinition = "varchar(20) default 'city1'")
    private String city;

    @Column(columnDefinition = "varchar(20) default 'street1'")
    private String street;

    @Column( columnDefinition = "varchar(20) default 'zipcode1'")
    private String zipcode;

}
