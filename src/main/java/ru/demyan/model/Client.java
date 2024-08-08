package ru.demyan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "client")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String email;
    private List<Long>services;

    public Client(Map<String, String> mp) {
        this.firstName = mp.get("first_name");
        this.middleName = mp.get("middle_name");
        this.lastName = mp.get("last_name");
        this.phone = mp.get("phone");
        this.email = mp.get("email");
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + middleName + ", " + phone + ", " + email;
    }
}
