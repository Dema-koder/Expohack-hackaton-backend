package ru.demyan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Table(name = "service")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String company;
    private String service;
    private String tariff;

    public Service(Map<String, String> mp) {
        this.company = mp.get("company");
        this.service = mp.get("service");
        this.tariff = mp.get("tariff");
    }

    @Override
    public String toString() {
        return company + " " + service + " " + tariff;
    }
}
