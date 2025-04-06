package build.master.service;

import build.master.dto.MotherboardDTO;
import build.master.model.entities.Motherboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MotherboardService {
    Page<Motherboard> getAllMotherboards(Pageable pageable);

    Page<Motherboard> getAllMotherboards(String socket, String chipset, String manufacturer, Pageable pageable);

    Optional<Motherboard> getMotherboardById(Long id);

    Page<Motherboard> getMotherboardsBySocket(String socket, Pageable pageable);
    Page<Motherboard> getMotherboardsByChipset(String chipset, Pageable pageable);
    Page<Motherboard> getMotherboardsByFormFactor(String formFactor, Pageable pageable);
    Page<Motherboard> getMotherboardsByMemoryType(String memoryType, Pageable pageable);
    Page<Motherboard> getMotherboardsByManufacturer(String manufacturer, Pageable pageable);

    List<String> getAllManufacturers();

    Motherboard mapToEntity(MotherboardDTO dto);

    MotherboardDTO mapToDTO(Motherboard motherboard);

    Motherboard saveMotherboard(Motherboard motherboard);
    Motherboard updateMotherboard(Long id, Motherboard motherboard);
    Motherboard updateMotherboard(Long id, MotherboardDTO motherboardDTO);
    void deleteMotherboard(Long id);
}

