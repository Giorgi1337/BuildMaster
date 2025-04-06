package build.master.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "motherboards")
@AllArgsConstructor
@RequiredArgsConstructor
public class Motherboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String manufacturer;

    private String model;
    private Double price;

    @Lob
    private byte[] image;

    private String socket;
    private String chipset;
    private String formFactor;
    private String memoryType;
    private Integer memorySlots;
    private Integer maxMemory;
    private Integer sataConnectors;
    private Integer m2Slots;
    private Integer pciExpressSlots;
    private Boolean wifi;
    private Boolean bluetooth;
}