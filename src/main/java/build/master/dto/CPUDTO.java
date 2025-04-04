package build.master.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CPUDTO {
    private Long id;
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 255, message = "Name should be between 1 and 255 characters")
    private String name;

    private String model;

    @NotBlank(message = "Manufacturer is required")
    @Size(min = 1, max = 255, message = "Manufacturer should be between 1 and 255 characters")
    private String manufacturer;

    private Double price;

    @NotNull(message = "Socket is required")
    @Size(min = 1, max = 255, message = "Socket should be between 1 and 255 characters")
    private String socket;

    @Positive(message = "Cores must be positive")
    private Integer cores;

    @Positive(message = "Threads must be positive")
    private Integer threads;

    @Positive(message = "Base clock must be positive")
    private Double baseClock;

    @Positive(message = "Boost clock must be positive")
    private Double boostClock;

    @Positive(message = "TDP must be positive")
    private Integer tdp;

    private Boolean integratedGraphics;

    private MultipartFile image;
}