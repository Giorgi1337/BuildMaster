package build.master.controller;

import build.master.dto.MotherboardDTO;
import build.master.model.entities.Motherboard;
import build.master.service.MotherboardService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MotherboardControllerTest {

    @Mock
    private MotherboardService motherboardService;

    @InjectMocks
    private MotherboardController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllMotherboards() throws Exception {
        // Prepare test data
        List<Motherboard> motherboards = Arrays.asList(
                createMotherboard(1L, "ASUS ROG Strix X570-E", "AMD", "X570"),
                createMotherboard(2L, "MSI MPG B550 Gaming Edge", "MSI", "B550")
        );
        Page<Motherboard> motherboardPage = new PageImpl<>(motherboards);
        List<String> manufacturers = Arrays.asList("ASUS", "MSI", "Gigabyte");

        // Mock service behavior
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        when(motherboardService.getAllMotherboards(pageable)).thenReturn(motherboardPage);
        when(motherboardService.getAllManufacturers()).thenReturn(manufacturers);

        // Perform request and validate
        mockMvc.perform(get("/motherboards"))
                .andExpect(status().isOk())
                .andExpect(view().name("motherboard/list"))
                .andExpect(model().attributeExists("motherboards"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("manufacturers"));

        // Verify service was called
        verify(motherboardService, times(1)).getAllMotherboards(any(Pageable.class));
        verify(motherboardService, times(1)).getAllManufacturers();
    }

    @Test
    public void testGetAllMotherboardsWithFilters() throws Exception {
        // Prepare test data
        List<Motherboard> motherboards = List.of(
                createMotherboard(1L, "ASUS ROG Strix X570-E", "ASUS", "X570")
        );
        Page<Motherboard> motherboardPage = new PageImpl<>(motherboards);
        List<String> manufacturers = Arrays.asList("ASUS", "MSI", "Gigabyte");

        // Mock service behavior
        when(motherboardService.getAllMotherboards(eq("AM4"), eq("X570"), eq("ASUS"), any(Pageable.class)))
                .thenReturn(motherboardPage);
        when(motherboardService.getAllManufacturers()).thenReturn(manufacturers);

        // Perform request and validate
        mockMvc.perform(get("/motherboards")
                        .param("socket", "AM4")
                        .param("chipset", "X570")
                        .param("manufacturer", "ASUS"))
                .andExpect(status().isOk())
                .andExpect(view().name("motherboard/list"))
                .andExpect(model().attributeExists("motherboards"));

        // Verify service was called with filters
        verify(motherboardService, times(1)).getAllMotherboards(
                eq("AM4"), eq("X570"), eq("ASUS"), any(Pageable.class));
    }

    @Test
    public void testGetMotherboardById_Found() throws Exception {
        // Prepare test data
        Motherboard motherboard = createMotherboard(1L, "ASUS ROG Strix X570-E", "ASUS", "X570");

        // Mock service behavior
        when(motherboardService.getMotherboardById(1L)).thenReturn(Optional.of(motherboard));

        // Perform request and validate
        mockMvc.perform(get("/motherboards/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("motherboard/motherboard-detail"))
                .andExpect(model().attributeExists("motherboard"));

        // Verify service was called
        verify(motherboardService, times(1)).getMotherboardById(1L);
    }

    @Test
    public void testGetMotherboardById_NotFound() throws Exception {
        // Mock service behavior - motherboard not found
        when(motherboardService.getMotherboardById(99L)).thenReturn(Optional.empty());

        // Perform request and validate - should redirect
        mockMvc.perform(get("/motherboards/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motherboards"));

        // Verify service was called
        verify(motherboardService, times(1)).getMotherboardById(99L);
    }

    @Test
    public void testShowCreateForm() throws Exception {
        // Mock service behavior
        List<String> manufacturers = Arrays.asList("ASUS", "MSI", "Gigabyte");
        when(motherboardService.getAllManufacturers()).thenReturn(manufacturers);

        // Perform request and validate
        mockMvc.perform(get("/motherboards/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("motherboard/add-motherboard"))
                .andExpect(model().attributeExists("motherboardDTO"))
                .andExpect(model().attributeExists("manufacturers"));

        // Verify service was called
        verify(motherboardService, times(1)).getAllManufacturers();
    }

    @Test
    public void testCreateMotherboard_Success() throws Exception {
        // Prepare test data
        MotherboardDTO dto = new MotherboardDTO();
        dto.setName("New Motherboard");
        Motherboard entity = new Motherboard();
        entity.setName("New Motherboard");

        // Mock service behavior
        when(motherboardService.mapToEntity(any(MotherboardDTO.class))).thenReturn(entity);
        when(motherboardService.saveMotherboard(any(Motherboard.class))).thenReturn(entity);

        // Perform request and validate
        mockMvc.perform(post("/motherboards/new")
                        .flashAttr("motherboardDTO", dto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motherboards"))
                .andExpect(flash().attributeExists("success"));

        // Verify service was called
        verify(motherboardService, times(1)).mapToEntity(any(MotherboardDTO.class));
        verify(motherboardService, times(1)).saveMotherboard(any(Motherboard.class));
    }

    @Test
    public void testCreateMotherboard_Error() throws Exception {
        // Prepare test data
        MotherboardDTO dto = new MotherboardDTO();

        // Mock service behavior - throw exception
        when(motherboardService.mapToEntity(any(MotherboardDTO.class))).thenThrow(new RuntimeException("Test error"));

        // Perform request and validate
        mockMvc.perform(post("/motherboards/new")
                        .flashAttr("motherboardDTO", dto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motherboards/new"))
                .andExpect(flash().attributeExists("error"));

        // Verify service was called
        verify(motherboardService, times(1)).mapToEntity(any(MotherboardDTO.class));
        verify(motherboardService, times(0)).saveMotherboard(any(Motherboard.class));
    }

    @Test
    public void testShowEditForm_Found() throws Exception {
        // Prepare test data
        Motherboard motherboard = createMotherboard(1L, "ASUS ROG Strix X570-E", "ASUS", "X570");
        MotherboardDTO dto = new MotherboardDTO();
        dto.setName("ASUS ROG Strix X570-E");
        List<String> manufacturers = Arrays.asList("ASUS", "MSI", "Gigabyte");

        // Mock service behavior
        when(motherboardService.getMotherboardById(1L)).thenReturn(Optional.of(motherboard));
        when(motherboardService.mapToDTO(any(Motherboard.class))).thenReturn(dto);
        when(motherboardService.getAllManufacturers()).thenReturn(manufacturers);

        // Perform request and validate
        mockMvc.perform(get("/motherboards/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("motherboard/edit-motherboard"))
                .andExpect(model().attributeExists("motherboardDTO"))
                .andExpect(model().attributeExists("motherboardId"))
                .andExpect(model().attributeExists("manufacturers"));

        // Verify service was called
        verify(motherboardService, times(1)).getMotherboardById(1L);
        verify(motherboardService, times(1)).mapToDTO(any(Motherboard.class));
        verify(motherboardService, times(1)).getAllManufacturers();
    }

    @Test
    public void testShowEditForm_NotFound() throws Exception {
        // Mock service behavior - motherboard not found
        when(motherboardService.getMotherboardById(99L)).thenReturn(Optional.empty());

        // Perform request and validate - should redirect
        mockMvc.perform(get("/motherboards/99/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motherboards"));

        // Verify service was called
        verify(motherboardService, times(1)).getMotherboardById(99L);
        verify(motherboardService, times(0)).mapToDTO(any(Motherboard.class));
    }

    @Test
    public void testUpdateMotherboard_Error() throws Exception {
        // Prepare test data
        MotherboardDTO dto = new MotherboardDTO();

        // Mock service behavior - throw exception
        doThrow(new RuntimeException("Test error"))
                .when(motherboardService).updateMotherboard(eq(1L), any(MotherboardDTO.class));

        // Perform request and validate
        mockMvc.perform(post("/motherboards/1/edit")
                        .flashAttr("motherboardDTO", dto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motherboards/1/edit"))
                .andExpect(flash().attributeExists("error"));

        // Verify service was called
        verify(motherboardService, times(1)).updateMotherboard(eq(1L), any(MotherboardDTO.class));
    }

    @Test
    public void testDeleteMotherboard_Success() throws Exception {
        // Mock service behavior
        doNothing().when(motherboardService).deleteMotherboard(1L);

        // Perform request and validate
        mockMvc.perform(post("/motherboards/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motherboards"))
                .andExpect(flash().attributeExists("success"));

        // Verify service was called
        verify(motherboardService, times(1)).deleteMotherboard(1L);
    }

    @Test
    public void testGetMotherboardImage_Found() {
        // Prepare test data
        Motherboard motherboard = new Motherboard();
        motherboard.setId(1L);
        motherboard.setImage(new byte[]{1, 2, 3, 4});

        // Mock service behavior
        when(motherboardService.getMotherboardById(1L)).thenReturn(Optional.of(motherboard));

        // Execute controller method directly (since we need to check ResponseEntity)
        ResponseEntity<byte[]> response = controller.getMotherboardImage(1L);

        // Validate response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());
        assertNotNull(response.getBody());

        // Verify service was called
        verify(motherboardService, times(1)).getMotherboardById(1L);
    }

    @Test
    public void testGetMotherboardImage_NotFound() {
        // Mock service behavior - motherboard not found
        when(motherboardService.getMotherboardById(99L)).thenReturn(Optional.empty());

        // Execute controller method directly
        ResponseEntity<byte[]> response = controller.getMotherboardImage(99L);

        // Validate response
        assertEquals(404, response.getStatusCodeValue());

        // Verify service was called
        verify(motherboardService, times(1)).getMotherboardById(99L);
    }

    @Test
    public void testGetMotherboardImage_NoImage() {
        // Prepare test data - motherboard without image
        Motherboard motherboard = new Motherboard();
        motherboard.setId(1L);
        motherboard.setImage(null);

        // Mock service behavior
        when(motherboardService.getMotherboardById(1L)).thenReturn(Optional.of(motherboard));

        // Execute controller method directly
        ResponseEntity<byte[]> response = controller.getMotherboardImage(1L);

        // Validate response
        assertEquals(404, response.getStatusCodeValue());

        // Verify service was called
        verify(motherboardService, times(1)).getMotherboardById(1L);
    }

    @Test
    public void testDeleteMotherboard_Error() throws Exception {
        // Mock service behavior - throw exception
        doThrow(new RuntimeException("Test error"))
                .when(motherboardService).deleteMotherboard(1L);

        // Perform request and validate
        mockMvc.perform(post("/motherboards/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motherboards"))
                .andExpect(flash().attributeExists("error"));

        // Verify service was called
        verify(motherboardService, times(1)).deleteMotherboard(1L);
    }

    // Helper method to create a Motherboard instance for testing
    private Motherboard createMotherboard(Long id, String name, String manufacturer, String chipset) {
        Motherboard motherboard = new Motherboard();
        motherboard.setId(id);
        motherboard.setName(name);
        motherboard.setManufacturer(manufacturer);
        motherboard.setSocket("AM4");
        motherboard.setChipset(chipset);
        return motherboard;
    }
}