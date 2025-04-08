package build.master.repository;

import build.master.model.entities.Motherboard;
import build.master.repositories.MotherboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MotherboardRepositoryTest {

    @Mock
    private MotherboardRepository motherboardRepository;

    private Motherboard asusMotherboard;
    private Motherboard msiMotherboard;
    private Motherboard gigabyteMotherboard;
    private List<Motherboard> motherboards;
    private Page<Motherboard> motherboardPage;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Create test motherboards
        asusMotherboard = new Motherboard();
        asusMotherboard.setId(1L);
        asusMotherboard.setName("ROG STRIX Z790-E");
        asusMotherboard.setManufacturer("ASUS");
        asusMotherboard.setSocket("LGA1700");
        asusMotherboard.setChipset("Z790");
        asusMotherboard.setFormFactor("ATX");
        asusMotherboard.setMemoryType("DDR5");

        msiMotherboard = new Motherboard();
        msiMotherboard.setId(2L);
        msiMotherboard.setName("MPG Z790 CARBON");
        msiMotherboard.setManufacturer("MSI");
        msiMotherboard.setSocket("LGA1700");
        msiMotherboard.setChipset("Z790");
        msiMotherboard.setFormFactor("ATX");
        msiMotherboard.setMemoryType("DDR5");

        gigabyteMotherboard = new Motherboard();
        gigabyteMotherboard.setId(3L);
        gigabyteMotherboard.setName("B650 AORUS ELITE");
        gigabyteMotherboard.setManufacturer("Gigabyte");
        gigabyteMotherboard.setSocket("AM5");
        gigabyteMotherboard.setChipset("B650");
        gigabyteMotherboard.setFormFactor("mATX");
        gigabyteMotherboard.setMemoryType("DDR5");

        motherboards = Arrays.asList(asusMotherboard, msiMotherboard, gigabyteMotherboard);
        pageable = PageRequest.of(0, 10);
        motherboardPage = new PageImpl<>(motherboards, pageable, motherboards.size());
    }

    @Test
    void findAll() {
        // Given
        when(motherboardRepository.findAll(any(Pageable.class))).thenReturn(motherboardPage);

        // When
        Page<Motherboard> result = motherboardRepository.findAll(pageable);

        // Then
        assertEquals(3, result.getTotalElements());
        verify(motherboardRepository).findAll(pageable);
    }

    @Test
    void findBySocket() {
        // Given
        List<Motherboard> lga1700Motherboards = Arrays.asList(asusMotherboard, msiMotherboard);
        Page<Motherboard> lga1700Page = new PageImpl<>(lga1700Motherboards, pageable, lga1700Motherboards.size());

        when(motherboardRepository.findBySocket(eq("LGA1700"), any(Pageable.class))).thenReturn(lga1700Page);

        // When
        Page<Motherboard> result = motherboardRepository.findBySocket("LGA1700", pageable);

        // Then
        assertEquals(2, result.getTotalElements());
        verify(motherboardRepository).findBySocket("LGA1700", pageable);
    }

    @Test
    void findByChipset() {
        // Given
        List<Motherboard> z790Motherboards = Arrays.asList(asusMotherboard, msiMotherboard);
        Page<Motherboard> z790Page = new PageImpl<>(z790Motherboards, pageable, z790Motherboards.size());

        when(motherboardRepository.findByChipset(eq("Z790"), any(Pageable.class))).thenReturn(z790Page);

        // When
        Page<Motherboard> result = motherboardRepository.findByChipset("Z790", pageable);

        // Then
        assertEquals(2, result.getTotalElements());
        verify(motherboardRepository).findByChipset("Z790", pageable);
    }

    @Test
    void findByFormFactor() {
        // Given
        List<Motherboard> atxMotherboards = Arrays.asList(asusMotherboard, msiMotherboard);
        Page<Motherboard> atxPage = new PageImpl<>(atxMotherboards, pageable, atxMotherboards.size());

        when(motherboardRepository.findByFormFactor(eq("ATX"), any(Pageable.class))).thenReturn(atxPage);

        // When
        Page<Motherboard> result = motherboardRepository.findByFormFactor("ATX", pageable);

        // Then
        assertEquals(2, result.getTotalElements());
        verify(motherboardRepository).findByFormFactor("ATX", pageable);
    }

    @Test
    void findByMemoryType() {
        // Given
        when(motherboardRepository.findByMemoryType(eq("DDR5"), any(Pageable.class))).thenReturn(motherboardPage);

        // When
        Page<Motherboard> result = motherboardRepository.findByMemoryType("DDR5", pageable);

        // Then
        assertEquals(3, result.getTotalElements());
        verify(motherboardRepository).findByMemoryType("DDR5", pageable);
    }

    @Test
    void findByManufacturer() {
        // Given
        List<Motherboard> asusMotherboards = Arrays.asList(asusMotherboard);
        Page<Motherboard> asusPage = new PageImpl<>(asusMotherboards, pageable, asusMotherboards.size());

        when(motherboardRepository.findByManufacturer(eq("ASUS"), any(Pageable.class))).thenReturn(asusPage);

        // When
        Page<Motherboard> result = motherboardRepository.findByManufacturer("ASUS", pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(motherboardRepository).findByManufacturer("ASUS", pageable);
    }

    @Test
    void findDistinctManufacturers() {
        // Given
        List<String> manufacturers = Arrays.asList("ASUS", "MSI", "Gigabyte");
        when(motherboardRepository.findDistinctManufacturers()).thenReturn(manufacturers);

        // When
        List<String> result = motherboardRepository.findDistinctManufacturers();

        // Then
        assertEquals(3, result.size());
        assertTrue(result.contains("ASUS"));
        assertTrue(result.contains("MSI"));
        assertTrue(result.contains("Gigabyte"));
        verify(motherboardRepository).findDistinctManufacturers();
    }

    @Test
    void testSpecificationQuery() {
        // Given
        List<Motherboard> filteredMotherboards = Arrays.asList(asusMotherboard);
        Page<Motherboard> filteredPage = new PageImpl<>(filteredMotherboards, pageable, filteredMotherboards.size());

        when(motherboardRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(filteredPage);

        // When
        Page<Motherboard> result = motherboardRepository.findAll(
                (Specification<Motherboard>) (root, query, cb) -> cb.equal(root.get("manufacturer"), "ASUS"),
                pageable
        );

        // Then
        assertEquals(1, result.getTotalElements());
        verify(motherboardRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void findById() {
        // Given
        when(motherboardRepository.findById(1L)).thenReturn(Optional.of(asusMotherboard));

        // When
        Optional<Motherboard> result = motherboardRepository.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("ASUS", result.get().getManufacturer());
        verify(motherboardRepository).findById(1L);
    }

    @Test
    void save() {
        // Given
        when(motherboardRepository.save(any(Motherboard.class))).thenReturn(asusMotherboard);

        // When
        Motherboard result = motherboardRepository.save(asusMotherboard);

        // Then
        assertNotNull(result);
        assertEquals("ASUS", result.getManufacturer());
        verify(motherboardRepository).save(asusMotherboard);
    }

    @Test
    void deleteById() {
        // Given
        doNothing().when(motherboardRepository).deleteById(anyLong());

        // When
        motherboardRepository.deleteById(1L);

        // Then
        verify(motherboardRepository).deleteById(1L);
    }

    @Test
    void count() {
        // Given
        when(motherboardRepository.count()).thenReturn(3L);

        // When
        long count = motherboardRepository.count();

        // Then
        assertEquals(3, count);
        verify(motherboardRepository).count();
    }

    @Test
    void existsById() {
        // Given
        when(motherboardRepository.existsById(1L)).thenReturn(true);
        when(motherboardRepository.existsById(99L)).thenReturn(false);

        // When
        boolean exists1 = motherboardRepository.existsById(1L);
        boolean exists99 = motherboardRepository.existsById(99L);

        // Then
        assertTrue(exists1);
        assertFalse(exists99);
        verify(motherboardRepository).existsById(1L);
        verify(motherboardRepository).existsById(99L);
    }

    @Test
    void saveAll() {
        // Given
        when(motherboardRepository.saveAll(anyList())).thenReturn(motherboards);

        // When
        List<Motherboard> result = motherboardRepository.saveAll(motherboards);

        // Then
        assertEquals(3, result.size());
        verify(motherboardRepository).saveAll(motherboards);
    }

    @Test
    void findAllWithSpecification() {
        // Given
        when(motherboardRepository.findAll(any(Specification.class))).thenReturn(motherboards);

        // When
        List<Motherboard> result = motherboardRepository.findAll(
                (Specification<Motherboard>) (root, query, cb) -> cb.equal(root.get("memoryType"), "DDR5")
        );

        // Then
        assertEquals(3, result.size());
        verify(motherboardRepository).findAll(any(Specification.class));
    }
}