package com.uz.pdfgenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_table")
@SQLRestriction("deleted_at is null")
public class UserEntity extends BaseEntity {

    private String pinfl;

    private String fullName;

    private LocalDate birthDate;

    private String gender;

    private String passSeries;

    private String phone;

    private String address;
}

