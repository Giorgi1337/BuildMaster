package build.master.service.impl;

import build.master.dto.CPUDTO;
import build.master.model.entities.CPU;
import build.master.repositories.CPURepository;
import build.master.service.CPUService;

import build.master.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CPUServiceImpl implements CPUService {

    private final CPURepository cpuRepository;

    public CPUServiceImpl(CPURepository cpuRepository) {
        this.cpuRepository = cpuRepository;
    }

    @Override
    public Page<CPU> getAllCPUs(Pageable pageable) {
        return cpuRepository.findAll(pageable);
    }

    @Override
    public Optional<CPU> getCPUById(Long id) {
        return cpuRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CPU> getCPUsBySocket(String socket, Pageable pageable) {
        return cpuRepository.findBySocket(socket, pageable);
    }

    @Override
    public Page<CPU> getCPUsByManufacturer(String manufacturer, Pageable pageable) {
        return cpuRepository.findByManufacturer(manufacturer, pageable);
    }

    @Override
    public Page<CPU> getCPUsByCores(Integer cores, Pageable pageable) {
        return cpuRepository.findByCoresGreaterThanEqual(cores, pageable);
    }

    @Override
    @Transactional
    public CPU mapToEntity(CPUDTO cpuCreateDTO) {
        return Utils.mapToEntity(cpuCreateDTO, CPU.class);
    }

    @Override
    public CPUDTO mapToDTO(CPU cpu) {
        return Utils.mapToDTO(cpu, CPUDTO.class);
    }

    @Override
    public List<String> getAllManufacturers() {
        return cpuRepository.findDistinctManufacturers();
    }

    @Override
    @Transactional
    public CPU updateCPU(Long id, CPUDTO cpuDTO) {
        return cpuRepository.findById(id)
                .map(existingCPU -> {
                    existingCPU.setName(cpuDTO.getName());
                    existingCPU.setModel(cpuDTO.getModel());
                    existingCPU.setManufacturer(cpuDTO.getManufacturer());
                    existingCPU.setPrice(cpuDTO.getPrice());
                    existingCPU.setSocket(cpuDTO.getSocket());
                    existingCPU.setCores(cpuDTO.getCores());
                    existingCPU.setThreads(cpuDTO.getThreads());
                    existingCPU.setTdp(cpuDTO.getTdp());
                    existingCPU.setBaseClock(cpuDTO.getBaseClock());
                    existingCPU.setBoostClock(cpuDTO.getBoostClock());
                    existingCPU.setIntegratedGraphics(cpuDTO.getIntegratedGraphics());

                    return cpuRepository.save(existingCPU);
                })
                .orElseThrow(() -> new EntityNotFoundException("CPU with ID " + id + " not found"));
    }

    @Override
    @Transactional
    public CPU saveCPU(CPU cpu) {
        if (cpu.getImage() == null || cpu.getImage().length == 0) {
            cpu.setImage(Utils.getDefaultImage());
        } else {
            cpu.setImage(Utils.resizeImage(cpu.getImage()));
        }

        return cpuRepository.save(cpu);
    }

    @Override
    @Transactional
    public void deleteCPU(Long id) {
        cpuRepository.deleteById(id);
    }
}
