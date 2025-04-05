package build.master.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "cpus")
@AllArgsConstructor
@RequiredArgsConstructor
public class CPU {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String manufacturer;

    private String model;
    private Double price;
    private String socket;
    private Integer cores;
    private Integer threads;
    private Double baseClock;
    private Double boostClock;
    private Integer tdp;
    private String architecture;
    private Boolean integratedGraphics;

    @Lob
    private byte[] image;
}