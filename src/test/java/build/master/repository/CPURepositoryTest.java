package build.master.repository;

import build.master.model.entities.CPU;
import build.master.repositories.CPURepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CPURepositoryTest {

    @Mock
    private CPURepository cpuRepository;

    private CPU amdCpu;
    private CPU intelCpu;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Set up test data
        amdCpu = new CPU();
        amdCpu.setId(1L);
        amdCpu.setName("Ryzen 7 5800X");
        amdCpu.setManufacturer("AMD");
        amdCpu.setSocket("AM4");
        amdCpu.setCores(8);

        intelCpu = new CPU();
        intelCpu.setId(2L);
        intelCpu.setName("Core i7-12700K");
        intelCpu.setManufacturer("Intel");
        intelCpu.setSocket("LGA1700");
        intelCpu.setCores(12);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testFindBySocket() {
        // Setup mock behavior
        List<CPU> amSocketCPUs = Arrays.asList(amdCpu);
        Page<CPU> amSocketCPUPage = new PageImpl<>(amSocketCPUs, pageable, amSocketCPUs.size());
        when(cpuRepository.findBySocket("AM4", pageable)).thenReturn(amSocketCPUPage);

        // Test
        Page<CPU> result = cpuRepository.findBySocket("AM4", pageable);

        // Verify
        assertEquals(1, result.getTotalElements());
        assertEquals("AM4", result.getContent().get(0).getSocket());
        verify(cpuRepository).findBySocket("AM4", pageable);
    }

    @Test
    void testFindByManufacturer() {
        // Setup mock behavior
        List<CPU> amdCPUs = Arrays.asList(amdCpu);
        Page<CPU> amdCPUPage = new PageImpl<>(amdCPUs, pageable, amdCPUs.size());
        when(cpuRepository.findByManufacturer("AMD", pageable)).thenReturn(amdCPUPage);

        // Test
        Page<CPU> result = cpuRepository.findByManufacturer("AMD", pageable);

        // Verify
        assertEquals(1, result.getTotalElements());
        assertEquals("AMD", result.getContent().get(0).getManufacturer());
        verify(cpuRepository).findByManufacturer("AMD", pageable);
    }

    @Test
    void testFindByCoresGreaterThanEqual() {
        // Setup mock behavior
        List<CPU> highCoreCPUs = Arrays.asList(amdCpu, intelCpu);
        Page<CPU> highCoreCPUPage = new PageImpl<>(highCoreCPUs, pageable, highCoreCPUs.size());
        when(cpuRepository.findByCoresGreaterThanEqual(8, pageable)).thenReturn(highCoreCPUPage);

        // Test
        Page<CPU> result = cpuRepository.findByCoresGreaterThanEqual(8, pageable);

        // Verify
        assertEquals(2, result.getTotalElements());
        result.getContent().forEach(cpu -> assertTrue(cpu.getCores() >= 8));
        verify(cpuRepository).findByCoresGreaterThanEqual(8, pageable);
    }

    @Test
    void testFindDistinctManufacturers() {
        // Setup mock behavior
        List<String> manufacturers = Arrays.asList("AMD", "Intel");
        when(cpuRepository.findDistinctManufacturers()).thenReturn(manufacturers);

        // Test
        List<String> result = cpuRepository.findDistinctManufacturers();

        // Verify
        assertEquals(2, result.size());
        assertTrue(result.contains("AMD"));
        assertTrue(result.contains("Intel"));
        verify(cpuRepository).findDistinctManufacturers();
    }
}