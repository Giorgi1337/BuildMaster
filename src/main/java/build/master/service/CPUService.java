package build.master.service;

import build.master.dto.CPUDTO;
import build.master.model.entities.CPU;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CPUService {
    Page<CPU> getAllCPUs(Pageable pageable);
    Optional<CPU> getCPUById(Long id);
    Page<CPU> getCPUsBySocket(String socket, Pageable pageable);
    Page<CPU> getCPUsByManufacturer(String manufacturer, Pageable pageable);
    Page<CPU> getCPUsByCores(Integer cores, Pageable pageable);
    CPU mapToEntity(CPUDTO cpuCreateDTO);
    CPUDTO mapToDTO(CPU cpu);
    List<String> getAllManufacturers();
    CPU updateCPU(Long id, CPUDTO cpuDTO);
    CPU saveCPU(CPU cpu);
    void deleteCPU(Long id);
}