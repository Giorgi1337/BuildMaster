package build.master.service;

import build.master.dto.MotherboardDTO;
import build.master.model.entities.Motherboard;
import build.master.repositories.MotherboardRepository;
import build.master.service.impl.MotherboardServiceImpl;
import build.master.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MotherboardServiceImplTest {

    @Mock
    private MotherboardRepository motherboardRepository;

    @InjectMocks
    private MotherboardServiceImpl motherboardService;

    private Motherboard motherboard;
    private MotherboardDTO motherboardDTO;
    private Page<Motherboard> motherboardPage;
    private Pageable pageable;
    private byte[] imageBytes;

    @BeforeEach
    void setUp() {
        // Setup test data
        motherboard = new Motherboard();
        motherboard.setId(1L);
        motherboard.setName("Test Motherboard");
        motherboard.setManufacturer("ASUS");
        motherboard.setModel("ROG STRIX Z790-E GAMING WIFI");
        motherboard.setPrice(399.99);
        motherboard.setSocket("LGA1700");
        motherboard.setChipset("Z790");
        motherboard.setFormFactor("ATX");
        motherboard.setMemoryType("DDR5");
        motherboard.setMemorySlots(4);
        motherboard.setMaxMemory(128);
        motherboard.setSataConnectors(6);
        motherboard.setM2Slots(4);
        motherboard.setPciExpressSlots(3);
        motherboard.setWifi(true);
        motherboard.setBluetooth(true);
        imageBytes = "test image content".getBytes();
        motherboard.setImage(imageBytes);

        motherboardDTO = new MotherboardDTO();
        motherboardDTO.setId(1L);
        motherboardDTO.setName("Test Motherboard");
        motherboardDTO.setManufacturer("ASUS");
        motherboardDTO.setModel("ROG STRIX Z790-E GAMING WIFI");
        motherboardDTO.setPrice(399.99);
        motherboardDTO.setSocket("LGA1700");
        motherboardDTO.setChipset("Z790");
        motherboardDTO.setFormFactor("ATX");
        motherboardDTO.setMemoryType("DDR5");
        motherboardDTO.setMemorySlots(4);
        motherboardDTO.setMaxMemory(128);
        motherboardDTO.setSataConnectors(6);
        motherboardDTO.setM2Slots(4);
        motherboardDTO.setPciExpressSlots(3);
        motherboardDTO.setWifi(true);
        motherboardDTO.setBluetooth(true);

        List<Motherboard> motherboardList = Arrays.asList(motherboard);
        motherboardPage = new PageImpl<>(motherboardList);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getAllMotherboards() {
        // Given
        when(motherboardRepository.findAll(any(Pageable.class))).thenReturn(motherboardPage);

        // When
        Page<Motherboard> result = motherboardService.getAllMotherboards(pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Motherboard", result.getContent().get(0).getName());
        verify(motherboardRepository).findAll(pageable);
    }

    @Test
    void getAllMotherboardsWithFilters() {
        // Given
        when(motherboardRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(motherboardPage);

        // When
        Page<Motherboard> result = motherboardService.getAllMotherboards("LGA1700", "Z790", "ASUS", pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(motherboardRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getMotherboardById() {
        // Given
        when(motherboardRepository.findById(1L)).thenReturn(Optional.of(motherboard));

        // When
        Optional<Motherboard> result = motherboardService.getMotherboardById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Motherboard", result.get().getName());
        verify(motherboardRepository).findById(1L);
    }

    @Test
    void getMotherboardsBySocket() {
        // Given
        when(motherboardRepository.findBySocket(anyString(), any(Pageable.class))).thenReturn(motherboardPage);

        // When
        Page<Motherboard> result = motherboardService.getMotherboardsBySocket("LGA1700", pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(motherboardRepository).findBySocket("LGA1700", pageable);
    }

    @Test
    void getMotherboardsByChipset() {
        // Given
        when(motherboardRepository.findByChipset(anyString(), any(Pageable.class))).thenReturn(motherboardPage);

        // When
        Page<Motherboard> result = motherboardService.getMotherboardsByChipset("Z790", pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(motherboardRepository).findByChipset("Z790", pageable);
    }

    @Test
    void getMotherboardsByFormFactor() {
        // Given
        when(motherboardRepository.findByFormFactor(anyString(), any(Pageable.class))).thenReturn(motherboardPage);

        // When
        Page<Motherboard> result = motherboardService.getMotherboardsByFormFactor("ATX", pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(motherboardRepository).findByFormFactor("ATX", pageable);
    }

    @Test
    void getMotherboardsByMemoryType() {
        // Given
        when(motherboardRepository.findByMemoryType(anyString(), any(Pageable.class))).thenReturn(motherboardPage);

        // When
        Page<Motherboard> result = motherboardService.getMotherboardsByMemoryType("DDR5", pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(motherboardRepository).findByMemoryType("DDR5", pageable);
    }

    @Test
    void getMotherboardsByManufacturer() {
        // Given
        when(motherboardRepository.findByManufacturer(anyString(), any(Pageable.class))).thenReturn(motherboardPage);

        // When
        Page<Motherboard> result = motherboardService.getMotherboardsByManufacturer("ASUS", pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(motherboardRepository).findByManufacturer("ASUS", pageable);
    }

    @Test
    void getAllManufacturers() {
        // Given
        List<String> manufacturers = Arrays.asList("ASUS", "Gigabyte", "MSI");
        when(motherboardRepository.findDistinctManufacturers()).thenReturn(manufacturers);

        // When
        List<String> result = motherboardService.getAllManufacturers();

        // Then
        assertEquals(3, result.size());
        assertTrue(result.contains("ASUS"));
        verify(motherboardRepository).findDistinctManufacturers();
    }

    @Test
    void mapToEntity() throws IOException {
        // Given
        MockMultipartFile mockImage = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image content".getBytes());
        motherboardDTO.setImage(mockImage);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapToEntity(any(MotherboardDTO.class), eq(Motherboard.class)))
                    .thenReturn(motherboard);

            // When
            Motherboard result = motherboardService.mapToEntity(motherboardDTO);

            // Then
            assertNotNull(result);
            assertArrayEquals("test image content".getBytes(), result.getImage());
            mockedUtils.verify(() -> Utils.mapToEntity(motherboardDTO, Motherboard.class));
        }
    }

    @Test
    void mapToEntityWithoutImage() {
        // Given
        motherboardDTO.setImage(null);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapToEntity(any(MotherboardDTO.class), eq(Motherboard.class)))
                    .thenReturn(motherboard);

            // When
            Motherboard result = motherboardService.mapToEntity(motherboardDTO);

            // Then
            assertNotNull(result);
            mockedUtils.verify(() -> Utils.mapToEntity(motherboardDTO, Motherboard.class));
        }
    }

    @Test
    void mapToEntityWithImageIOException() throws IOException {
        // Given
        MultipartFile mockImage = mock(MultipartFile.class);
        motherboardDTO.setImage(mockImage);

        when(mockImage.isEmpty()).thenReturn(false);
        when(mockImage.getBytes()).thenThrow(new IOException("Test exception"));

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapToEntity(any(MotherboardDTO.class), eq(Motherboard.class)))
                    .thenReturn(motherboard);

            // When & Then
            assertThrows(RuntimeException.class, () -> motherboardService.mapToEntity(motherboardDTO));
        }
    }

    @Test
    void mapToDTO() {
        // Given
        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapToDTO(any(Motherboard.class), eq(MotherboardDTO.class)))
                    .thenReturn(motherboardDTO);

            // When
            MotherboardDTO result = motherboardService.mapToDTO(motherboard);

            // Then
            assertNotNull(result);
            assertEquals("Test Motherboard", result.getName());
            mockedUtils.verify(() -> Utils.mapToDTO(motherboard, MotherboardDTO.class));
        }
    }

    @Test
    void saveMotherboard() {
        // Given
        when(motherboardRepository.save(any(Motherboard.class))).thenReturn(motherboard);

        // When
        Motherboard result = motherboardService.saveMotherboard(motherboard);

        // Then
        assertNotNull(result);
        assertEquals("Test Motherboard", result.getName());
        verify(motherboardRepository).save(motherboard);
    }

    @Test
    void updateMotherboardWithEntity() {
        // Given
        Motherboard updatedMotherboard = new Motherboard();
        updatedMotherboard.setName("Updated Motherboard");
        updatedMotherboard.setManufacturer("MSI");
        updatedMotherboard.setImage(new byte[0]); // Empty image array

        when(motherboardRepository.findById(1L)).thenReturn(Optional.of(motherboard));
        when(motherboardRepository.save(any(Motherboard.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Motherboard result = motherboardService.updateMotherboard(1L, updatedMotherboard);

        // Then
        assertNotNull(result);
        assertEquals("Updated Motherboard", result.getName());
        assertEquals("MSI", result.getManufacturer());
        // Should keep the original image since the updated one is empty
        assertArrayEquals(imageBytes, result.getImage());
        verify(motherboardRepository).findById(1L);
        verify(motherboardRepository).save(any(Motherboard.class));
    }

    @Test
    void updateMotherboardWithEntityNotFound() {
        // Given
        when(motherboardRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> motherboardService.updateMotherboard(99L, new Motherboard()));
        verify(motherboardRepository).findById(99L);
        verify(motherboardRepository, never()).save(any(Motherboard.class));
    }

    @Test
    void updateMotherboardWithDTO() throws IOException {
        // Given
        MockMultipartFile mockImage = new MockMultipartFile(
                "image", "updated.jpg", "image/jpeg", "updated image content".getBytes());
        motherboardDTO.setName("Updated Motherboard");
        motherboardDTO.setImage(mockImage);

        Motherboard mappedMotherboard = new Motherboard();
        mappedMotherboard.setName("Updated Motherboard");

        when(motherboardRepository.findById(1L)).thenReturn(Optional.of(motherboard));

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapToEntity(any(MotherboardDTO.class), eq(Motherboard.class)))
                    .thenReturn(mappedMotherboard);

            when(motherboardRepository.save(any(Motherboard.class))).thenAnswer(i -> i.getArguments()[0]);

            // When
            Motherboard result = motherboardService.updateMotherboard(1L, motherboardDTO);

            // Then
            assertNotNull(result);
            assertEquals("Updated Motherboard", result.getName());
            assertArrayEquals("updated image content".getBytes(), result.getImage());
            assertEquals(1L, result.getId()); // ID should be preserved

            verify(motherboardRepository).findById(1L);
            verify(motherboardRepository).save(any(Motherboard.class));
        }
    }

    @Test
    void updateMotherboardWithDTONoImage() {
        // Given
        motherboardDTO.setName("Updated Motherboard");
        motherboardDTO.setImage(null);

        Motherboard mappedMotherboard = new Motherboard();
        mappedMotherboard.setName("Updated Motherboard");

        when(motherboardRepository.findById(1L)).thenReturn(Optional.of(motherboard));

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapToEntity(any(MotherboardDTO.class), eq(Motherboard.class)))
                    .thenReturn(mappedMotherboard);

            when(motherboardRepository.save(any(Motherboard.class))).thenAnswer(i -> i.getArguments()[0]);

            // When
            Motherboard result = motherboardService.updateMotherboard(1L, motherboardDTO);

            // Then
            assertNotNull(result);
            assertEquals("Updated Motherboard", result.getName());
            assertArrayEquals(imageBytes, result.getImage()); // Should keep the original image

            verify(motherboardRepository).findById(1L);
            verify(motherboardRepository).save(any(Motherboard.class));
        }
    }

    @Test
    void updateMotherboardWithDTOImageIOException() throws IOException {
        // Given
        MultipartFile mockImage = mock(MultipartFile.class);
        motherboardDTO.setImage(mockImage);

        when(motherboardRepository.findById(1L)).thenReturn(Optional.of(motherboard));
        when(mockImage.isEmpty()).thenReturn(false);
        when(mockImage.getBytes()).thenThrow(new IOException("Test exception"));

        Motherboard mappedMotherboard = new Motherboard();

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(() -> Utils.mapToEntity(any(MotherboardDTO.class), eq(Motherboard.class)))
                    .thenReturn(mappedMotherboard);

            // When & Then
            assertThrows(RuntimeException.class, () -> motherboardService.updateMotherboard(1L, motherboardDTO));
        }
    }

    @Test
    void updateMotherboardWithDTONotFound() {
        // Given
        when(motherboardRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> motherboardService.updateMotherboard(99L, motherboardDTO));
        verify(motherboardRepository).findById(99L);
        verify(motherboardRepository, never()).save(any(Motherboard.class));
    }

    @Test
    void deleteMotherboard() {
        // Given
        doNothing().when(motherboardRepository).deleteById(1L);

        // When
        motherboardService.deleteMotherboard(1L);

        // Then
        verify(motherboardRepository).deleteById(1L);
    }
}