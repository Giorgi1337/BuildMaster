package build.master.service;

import build.master.dto.CPUDTO;
import build.master.model.entities.CPU;
import build.master.repositories.CPURepository;
import build.master.service.impl.CPUServiceImpl;
import build.master.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CPUServiceImplTest {

    @Mock
    private CPURepository cpuRepository;

    @InjectMocks
    private CPUServiceImpl cpuService;

    private CPU cpu;
    private CPUDTO cpuDTO;
    private Page<CPU> cpuPage;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        cpu = new CPU();
        cpu.setId(1L);
        cpu.setName("Ryzen 7 5800X");
        cpu.setManufacturer("AMD");
        cpu.setModel("5800X");
        cpu.setPrice(349.99);
        cpu.setSocket("AM4");
        cpu.setCores(8);
        cpu.setThreads(16);
        cpu.setBaseClock(3.8);
        cpu.setBoostClock(4.7);
        cpu.setTdp(105);
        cpu.setArchitecture("Zen 3");
        cpu.setIntegratedGraphics(false);

        cpuDTO = new CPUDTO();
        cpuDTO.setName("Ryzen 7 5800X");
        cpuDTO.setManufacturer("AMD");
        cpuDTO.setModel("5800X");
        cpuDTO.setPrice(349.99);
        cpuDTO.setSocket("AM4");
        cpuDTO.setCores(8);
        cpuDTO.setThreads(16);
        cpuDTO.setBaseClock(3.8);
        cpuDTO.setBoostClock(4.7);
        cpuDTO.setTdp(105);
        cpuDTO.setIntegratedGraphics(false);

        List<CPU> cpuList = Arrays.asList(cpu);
        pageable = PageRequest.of(0, 10);
        cpuPage = new PageImpl<>(cpuList, pageable, cpuList.size());
    }

    @Test
    void testGetAllCPUs() {
        when(cpuRepository.findAll(pageable)).thenReturn(cpuPage);

        Page<CPU> result = cpuService.getAllCPUs(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Ryzen 7 5800X", result.getContent().get(0).getName());
        verify(cpuRepository).findAll(pageable);
    }

    @Test
    void testGetCPUById() {
        when(cpuRepository.findById(1L)).thenReturn(Optional.of(cpu));

        Optional<CPU> result = cpuService.getCPUById(1L);

        assertTrue(result.isPresent());
        assertEquals("Ryzen 7 5800X", result.get().getName());
        verify(cpuRepository).findById(1L);
    }

    @Test
    void testGetCPUsBySocket() {
        when(cpuRepository.findBySocket("AM4", pageable)).thenReturn(cpuPage);

        Page<CPU> result = cpuService.getCPUsBySocket("AM4", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("AM4", result.getContent().get(0).getSocket());
        verify(cpuRepository).findBySocket("AM4", pageable);
    }

    @Test
    void testGetCPUsByManufacturer() {
        when(cpuRepository.findByManufacturer("AMD", pageable)).thenReturn(cpuPage);

        Page<CPU> result = cpuService.getCPUsByManufacturer("AMD", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("AMD", result.getContent().get(0).getManufacturer());
        verify(cpuRepository).findByManufacturer("AMD", pageable);
    }

    @Test
    void testGetCPUsByCores() {
        when(cpuRepository.findByCoresGreaterThanEqual(8, pageable)).thenReturn(cpuPage);

        Page<CPU> result = cpuService.getCPUsByCores(8, pageable);

        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getCores() >= 8);
        verify(cpuRepository).findByCoresGreaterThanEqual(8, pageable);
    }

    @Test
    void testMapToEntity() {
        CPU result = cpuService.mapToEntity(cpuDTO);

        assertNotNull(result);
        assertEquals("Ryzen 7 5800X", result.getName());
        assertEquals("AMD", result.getManufacturer());
        assertEquals("5800X", result.getModel());
        assertEquals(349.99, result.getPrice());
        assertEquals("AM4", result.getSocket());
        assertEquals(8, result.getCores());
        assertEquals(16, result.getThreads());
    }

    @Test
    void testMapToDTO() {
        CPUDTO result = cpuService.mapToDTO(cpu);

        assertNotNull(result);
        assertEquals("Ryzen 7 5800X", result.getName());
        assertEquals("AMD", result.getManufacturer());
        assertEquals("5800X", result.getModel());
        assertEquals(349.99, result.getPrice());
        assertEquals("AM4", result.getSocket());
        assertEquals(8, result.getCores());
        assertEquals(16, result.getThreads());
    }

    @Test
    void testGetAllManufacturers() {
        List<String> manufacturers = Arrays.asList("AMD", "Intel");
        when(cpuRepository.findDistinctManufacturers()).thenReturn(manufacturers);

        List<String> result = cpuService.getAllManufacturers();

        assertEquals(2, result.size());
        assertTrue(result.contains("AMD"));
        assertTrue(result.contains("Intel"));
        verify(cpuRepository).findDistinctManufacturers();
    }

    @Test
    void testUpdateCPU_Success() {
        when(cpuRepository.findById(1L)).thenReturn(Optional.of(cpu));
        when(cpuRepository.save(any(CPU.class))).thenReturn(cpu);

        cpuDTO.setName("Ryzen 7 5800X3D");
        CPU result = cpuService.updateCPU(1L, cpuDTO);

        assertEquals("Ryzen 7 5800X3D", result.getName());
        verify(cpuRepository).findById(1L);
        verify(cpuRepository).save(any(CPU.class));
    }

    @Test
    void testUpdateCPU_NotFound() {
        when(cpuRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cpuService.updateCPU(999L, cpuDTO));
        verify(cpuRepository).findById(999L);
        verify(cpuRepository, never()).save(any(CPU.class));
    }

    @Test
    void testSaveCPU_WithImage() {
        byte[] imageData = new byte[]{1, 2, 3, 4};
        cpu.setImage(imageData);

        // Create a mockStatic for Utils class to mock the static methods
        try (var mockUtils = mockStatic(Utils.class)) {
            mockUtils.when(() -> Utils.resizeImage(any())).thenReturn(new byte[]{5, 6, 7, 8});
            when(cpuRepository.save(cpu)).thenReturn(cpu);

            CPU result = cpuService.saveCPU(cpu);

            assertNotNull(result);
            verify(cpuRepository).save(cpu);
            mockUtils.verify(() -> Utils.resizeImage(imageData));
            mockUtils.verify(Utils::getDefaultImage, never());
        }
    }

    @Test
    void testSaveCPU_WithoutImage() {
        cpu.setImage(null);

        // Create a mockStatic for Utils class to mock the static methods
        try (var mockUtils = mockStatic(Utils.class)) {
            mockUtils.when(Utils::getDefaultImage).thenReturn(new byte[]{1, 2, 3, 4});
            when(cpuRepository.save(cpu)).thenReturn(cpu);

            CPU result = cpuService.saveCPU(cpu);

            assertNotNull(result);
            verify(cpuRepository).save(cpu);
            mockUtils.verify(Utils::getDefaultImage);
            mockUtils.verify(() -> Utils.resizeImage(any()), never());
        }
    }

    @Test
    void testDeleteCPU() {
        doNothing().when(cpuRepository).deleteById(1L);

        cpuService.deleteCPU(1L);

        verify(cpuRepository).deleteById(1L);
    }

}
