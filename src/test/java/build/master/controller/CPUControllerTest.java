package build.master.controller;

import build.master.dto.CPUDTO;
import build.master.model.entities.CPU;
import build.master.service.CPUService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CPUControllerTest {

    @Mock
    private CPUService cpuService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CPUController cpuController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cpuController).build();
    }

    @Test
    public void testListCPUs_NoFilters() {
        // Create test CPUs with all fields initialized
        CPU cpu1 = createTestCPU(new CPU(1L, "Intel Core i7", "Intel", "LGA1700", 300.0, "LGA1700", 8, 16, 3.8, 5.0, 105, "x86_64", true, null));
        CPU cpu2 = createTestCPU(new CPU(2L, "AMD Ryzen 7", "AMD", "AM4", 350.0, "AM4", 8, 16, 3.6, 4.8, 95, "x86_64", false, null));
        List<CPU> cpuList = Arrays.asList(cpu1, cpu2);
        Page<CPU> cpuPage = new PageImpl<>(cpuList);
        List<String> manufacturers = Arrays.asList("Intel", "AMD");

        // Configure mock behavior
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        when(cpuService.getAllCPUs(pageable)).thenReturn(cpuPage);
        when(cpuService.getAllManufacturers()).thenReturn(manufacturers);

        // Execute controller method
        String viewName = cpuController.listCPUs(null, null, null, 0, 10, "name", "asc", model);

        // Verify the results
        assertEquals("cpu/cpus", viewName);
        verify(model).addAttribute("cpus", cpuList);
        verify(model).addAttribute("availableManufacturers", manufacturers);
        verify(model).addAttribute("totalPages", cpuPage.getTotalPages());
        verify(model).addAttribute("totalItems", cpuPage.getTotalElements());
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("sortBy", "name");
        verify(model).addAttribute("sortOrder", "asc");
    }

    @Test
    public void testListCPUs_FilterBySocket() {
        // Prepare test data using createTestCPU method
        CPU cpu1 = createTestCPU(new CPU(1L, "Intel Core i7", "Intel", "LGA1700", 300.0, "LGA1700", 8, 16, 3.8, 5.0, 105, "x86_64", true, null));
        List<CPU> cpuList = List.of(cpu1);
        Page<CPU> cpuPage = new PageImpl<>(cpuList);
        List<String> manufacturers = Arrays.asList("Intel", "AMD");

        // Configure mock behavior
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        when(cpuService.getCPUsBySocket("LGA1700", pageable)).thenReturn(cpuPage);
        when(cpuService.getAllManufacturers()).thenReturn(manufacturers);

        // Execute controller method
        String viewName = cpuController.listCPUs("LGA1700", null, null, 0, 10, "name", "asc", model);

        // Verify the results
        assertEquals("cpu/cpus", viewName);
        verify(model).addAttribute("cpus", cpuList);
        verify(model).addAttribute("availableManufacturers", manufacturers);
        verify(model).addAttribute("totalPages", cpuPage.getTotalPages());
        verify(model).addAttribute("totalItems", cpuPage.getTotalElements());
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("sortBy", "name");
        verify(model).addAttribute("sortOrder", "asc");
    }

    @Test
    public void testListCPUs_FilterByManufacturer() {
        // Prepare test data using createTestCPU method
        CPU cpu1 = createTestCPU(new CPU(1L, "Intel Core i7", "Intel", "LGA1700", 300.0, "LGA1700", 8, 16, 3.8, 5.0, 105, "x86_64", true, null));
        List<CPU> cpuList = List.of(cpu1);
        Page<CPU> cpuPage = new PageImpl<>(cpuList);
        List<String> manufacturers = Arrays.asList("Intel", "AMD");

        // Configure mock behavior
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        when(cpuService.getCPUsByManufacturer("Intel", pageable)).thenReturn(cpuPage);
        when(cpuService.getAllManufacturers()).thenReturn(manufacturers);

        // Execute controller method
        String viewName = cpuController.listCPUs(null, "Intel", null, 0, 10, "name", "asc", model);

        // Verify the results
        assertEquals("cpu/cpus", viewName);
        verify(model).addAttribute("cpus", cpuList);
        verify(model).addAttribute("availableManufacturers", manufacturers);
        verify(model).addAttribute("totalPages", cpuPage.getTotalPages());
        verify(model).addAttribute("totalItems", cpuPage.getTotalElements());
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("sortBy", "name");
        verify(model).addAttribute("sortOrder", "asc");
    }

    @Test
    public void testListCPUs_FilterByCores() {
        // Prepare test data using createTestCPU method
        CPU cpu1 = createTestCPU(new CPU(1L, "Intel Core i7", "Intel", "LGA1700", 300.0, "LGA1700", 8, 16, 3.8, 5.0, 105, "x86_64", true, null));
        CPU cpu2 = createTestCPU(new CPU(2L, "AMD Ryzen 7", "AMD", "AM4", 350.0, "AM4", 8, 16, 3.6, 4.8, 95, "x86_64", false, null));
        List<CPU> cpuList = Arrays.asList(cpu1, cpu2);
        Page<CPU> cpuPage = new PageImpl<>(cpuList);
        List<String> manufacturers = Arrays.asList("Intel", "AMD");

        // Configure mock behavior
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        when(cpuService.getCPUsByCores(8, pageable)).thenReturn(cpuPage);
        when(cpuService.getAllManufacturers()).thenReturn(manufacturers);

        // Execute controller method
        String viewName = cpuController.listCPUs(null, null, 8, 0, 10, "name", "asc", model);

        // Verify the results
        assertEquals("cpu/cpus", viewName);
        verify(model).addAttribute("cpus", cpuList);
        verify(model).addAttribute("availableManufacturers", manufacturers);
        verify(model).addAttribute("totalPages", cpuPage.getTotalPages());
        verify(model).addAttribute("totalItems", cpuPage.getTotalElements());
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("sortBy", "name");
        verify(model).addAttribute("sortOrder", "asc");
    }

    @Test
    public void testShowAddCPUForm() {
        // Execute controller method
        String viewName = cpuController.showAddCPUForm(model);

        // Verify the results
        assertEquals("cpu/add-cpu", viewName);
        verify(model).addAttribute(eq("cpuDTO"), any(CPUDTO.class));
    }

    @Test
    public void testAddCPU_Success() throws Exception {
        // Prepare test data
        CPUDTO baseCpuDTO = new CPUDTO(null, "Intel Core i9", null, "Intel", 299.99, "LGA1700", 16, 32, 3.5, 5.2, 125, true, null);
        CPUDTO cpuDTO = createTestCPUDTO(baseCpuDTO);

        CPU baseCpu = new CPU(1L, "Intel Core i9", "Intel", "Intel Core i9", 299.99, "LGA1700", 16, 32, 3.5, 5.2, 125, "x86_64", true, null);
        CPU cpu = createTestCPU(baseCpu);

        // Configure mock behavior
        when(bindingResult.hasErrors()).thenReturn(false);
        when(cpuService.mapToEntity(cpuDTO)).thenReturn(cpu);

        // Execute controller method
        String viewName = cpuController.addCPU(cpuDTO, bindingResult, model);

        // Verify the results
        assertEquals("redirect:/cpus", viewName);
        verify(cpuService).saveCPU(cpu);
    }

    @Test
    public void testAddCPU_ValidationErrors() {
        // Prepare test data
        CPUDTO cpuDTO = new CPUDTO();

        // Configure mock behavior
        when(bindingResult.hasErrors()).thenReturn(true);

        // Execute controller method
        String viewName = cpuController.addCPU(cpuDTO, bindingResult, model);

        // Verify the results
        assertEquals("cpu/add-cpu", viewName);
        verify(model).addAttribute("cpuDTO", cpuDTO);
        verify(cpuService, never()).saveCPU(any());
    }

    @Test
    public void testAddCPU_WithImage() throws Exception {
        // Prepare test data
        CPUDTO baseCpuDTO = new CPUDTO(null, "Intel Core i9", null, "Intel", 299.99, "LGA1700", 16, 32, 3.5, 5.2, 125, true, null);
        CPUDTO cpuDTO = createTestCPUDTO(baseCpuDTO);

        MockMultipartFile imageFile = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image content".getBytes()
        );
        cpuDTO.setImage(imageFile);

        CPU baseCpu = new CPU(1L, "Intel Core i9", "Intel", "Intel Core i9", 299.99, "LGA1700", 16, 32, 3.5, 5.2, 125, "x86_64", true, null);
        CPU cpu = createTestCPU(baseCpu);

        // Configure mock behavior
        when(bindingResult.hasErrors()).thenReturn(false);
        when(cpuService.mapToEntity(cpuDTO)).thenReturn(cpu);

        // Execute controller method
        String viewName = cpuController.addCPU(cpuDTO, bindingResult, model);

        // Verify the results
        assertEquals("redirect:/cpus", viewName);
        verify(cpuService).saveCPU(cpu);
        assertArrayEquals(imageFile.getBytes(), cpu.getImage());
    }

    @Test
    public void testViewCPUDetails_Found() {
        // Prepare test data
        CPU baseCpu = new CPU(1L, "Intel Core i7", "Intel", "Intel Core i7", 299.99, "LGA1700", 8, 16, 3.8, 5.0, 105, "x86_64", null, null);
        CPU cpu = createTestCPU(baseCpu);

        // Configure mock behavior
        when(cpuService.getCPUById(1L)).thenReturn(Optional.of(cpu));

        // Execute controller method
        String viewName = cpuController.viewCPUDetails(1L, model);

        // Verify the results
        assertEquals("cpu/cpu-details", viewName);
        verify(model).addAttribute("cpu", cpu);
        assertFalse(cpu.getIntegratedGraphics()); // Should default to false if null
    }

    @Test
    public void testViewCPUDetails_NotFound() {
        // Configure mock behavior
        when(cpuService.getCPUById(1L)).thenReturn(Optional.empty());

        // Execute controller method
        String viewName = cpuController.viewCPUDetails(1L, model);

        // Verify the results
        assertEquals("redirect:/cpus", viewName);
        verify(model, never()).addAttribute(eq("cpu"), any());
    }

    @Test
    public void testShowEditCPUForm_Found() {
        // Prepare test data
        CPU baseCpu = new CPU(1L, "Intel Core i7", "Intel", "Intel Core i7", 299.99, "LGA1700", 8, 16, 3.8, 5.0, 105, "x86_64", true, null);
        CPU cpu = createTestCPU(baseCpu);

        CPUDTO baseCpuDTO = new CPUDTO(1L, "Intel Core i7", null, "Intel", 299.99, "LGA1700", 8, 16, 3.8, 5.0, 105, true, null);
        CPUDTO cpuDTO = createTestCPUDTO(baseCpuDTO);

        // Configure mock behavior
        when(cpuService.getCPUById(1L)).thenReturn(Optional.of(cpu));
        when(cpuService.mapToDTO(cpu)).thenReturn(cpuDTO);

        // Execute controller method
        String viewName = cpuController.showEditCPUForm(1L, model);

        // Verify the results
        assertEquals("cpu/edit-cpu", viewName);
        verify(model).addAttribute("cpuDTO", cpuDTO);
        verify(model).addAttribute("cpuId", 1L);
    }

    @Test
    public void testShowEditCPUForm_NotFound() {
        // Configure mock behavior
        when(cpuService.getCPUById(1L)).thenReturn(Optional.empty());

        // Execute controller method
        String viewName = cpuController.showEditCPUForm(1L, model);

        // Verify the results
        assertEquals("redirect:/cpus", viewName);
        verify(model, never()).addAttribute(eq("cpuDTO"), any());
    }

    @Test
    public void testUpdateCPU_Success() throws Exception {
        // Prepare test data using updated createTestCPUDTO method
        CPUDTO baseCpuDTO = new CPUDTO(1L, "Intel Core i9 Updated", null, "Intel", 299.99, "LGA1700", 16, 32, 3.5, 5.2, 125, true, null);
        CPUDTO cpuDTO = createTestCPUDTO(baseCpuDTO);

        CPU baseCpu = new CPU(1L, "Intel Core i9 Updated", "Intel", "Intel Core i9 Updated", 299.99, "LGA1700", 16, 32, 3.5, 5.2, 125, "x86_64", true, null);
        CPU updatedCPU = createTestCPU(baseCpu);

        // Configure mock behavior
        when(bindingResult.hasErrors()).thenReturn(false);
        when(cpuService.updateCPU(eq(1L), any(CPUDTO.class))).thenReturn(updatedCPU);

        // Execute controller method
        String viewName = cpuController.updateCPU(1L, cpuDTO, bindingResult, model);

        // Verify the results
        assertEquals("redirect:/cpus", viewName);
        verify(cpuService).updateCPU(eq(1L), any(CPUDTO.class));
    }

    @Test
    public void testUpdateCPU_ValidationErrors() {
        // Prepare test data using createTestCPUDTO method with invalid data
        CPUDTO baseCpuDTO = new CPUDTO(1L, "", null, "", null, "", null, null, null, null, null, null, null);
        CPUDTO cpuDTO = createTestCPUDTO(baseCpuDTO);

        // Configure mock behavior
        when(bindingResult.hasErrors()).thenReturn(true);

        // Execute controller method
        String viewName = cpuController.updateCPU(1L, cpuDTO, bindingResult, model);

        // Verify the results
        assertEquals("cpu/edit-cpu", viewName);
        verify(model).addAttribute("cpuDTO", cpuDTO);
        verify(model).addAttribute("cpuId", 1L);
        verify(cpuService, never()).updateCPU(anyLong(), any(CPUDTO.class));
    }

    @Test
    public void testUpdateCPU_EntityNotFound() throws Exception {
        // Prepare test data
        CPUDTO baseCpuDTO = new CPUDTO(1L, "Intel Core i9 Updated", null, "Intel", 299.99, "LGA1700", 16, 32, 3.5, 5.2, 125, true, null);
        CPUDTO cpuDTO = createTestCPUDTO(baseCpuDTO);

        // Configure mock behavior
        when(bindingResult.hasErrors()).thenReturn(false);
        when(cpuService.updateCPU(eq(1L), any(CPUDTO.class))).thenThrow(new EntityNotFoundException("CPU not found"));

        // Execute controller method
        String viewName = cpuController.updateCPU(1L, cpuDTO, bindingResult, model);

        // Verify the results
        assertEquals("cpu/edit-cpu", viewName);
        verify(model).addAttribute(eq("errorMessage"), anyString());
    }

    @Test
    public void testDeleteCPU() {
        // Execute controller method
        String viewName = cpuController.deleteCpu(1L, model);

        // Verify the results
        assertEquals("redirect:/cpus", viewName);
        verify(cpuService).deleteCPU(1L);
        verify(model).addAttribute("message", "CPU successfully deleted.");
    }

    @Test
    public void testGetCPUImage_Found() {
        // Prepare test data
        CPU baseCpu = new CPU(1L, "Intel Core i7", "Intel", "Intel Core i7", 299.99, "LGA1700", 8, 16, 3.8, 5.0, 105, "x86_64", true, null);
        CPU cpu = createTestCPU(baseCpu);
        byte[] imageData = "test image data".getBytes();
        cpu.setImage(imageData);

        // Configure mock behavior
        when(cpuService.getCPUById(1L)).thenReturn(Optional.of(cpu));

        // Execute controller method
        ResponseEntity<byte[]> response = cpuController.getCPUImage(1L);

        // Verify the results
        assertEquals(200, response.getStatusCode().value());
        assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());
        assertArrayEquals(imageData, response.getBody());
    }

    private CPU createTestCPU(CPU cpu) {
        return new CPU(
                cpu.getId(),
                cpu.getName(),
                cpu.getManufacturer(),
                cpu.getModel(),
                cpu.getPrice(),
                cpu.getSocket(),
                cpu.getCores(),
                cpu.getThreads(),
                cpu.getBaseClock(),
                cpu.getBoostClock(),
                cpu.getTdp(),
                cpu.getArchitecture(),
                cpu.getIntegratedGraphics(),
                cpu.getImage()
        );
    }

    private CPUDTO createTestCPUDTO(CPUDTO cpuDTO) {
        return new CPUDTO(
                cpuDTO.getId(),
                cpuDTO.getName(),
                null,
                cpuDTO.getManufacturer(),
                299.99,
                cpuDTO.getSocket(),
                cpuDTO.getCores(),
                cpuDTO.getThreads(),
                cpuDTO.getBaseClock(),
                cpuDTO.getBoostClock(),
                cpuDTO.getTdp(),
                cpuDTO.getIntegratedGraphics(),
                null
        );
    }

}