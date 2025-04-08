package build.master.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class MotherboardDTO {

    private Long id;
    private String name;
    private String manufacturer;
    private String model;
    private Double price;
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
    private MultipartFile image;
}