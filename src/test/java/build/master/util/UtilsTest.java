package build.master.util;

import build.master.dto.CPUDTO;
import build.master.model.entities.CPU;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    @Test
    void testResizeImage() throws IOException {
        // Create a simple test image
        BufferedImage testImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(testImage, "jpg", baos);
        byte[] originalImageData = baos.toByteArray();

        // Test resize functionality
        byte[] resizedImageData = Utils.resizeImage(originalImageData);

        // Verify the resized image
        assertNotNull(resizedImageData);
        BufferedImage resizedImage = ImageIO.read(new ByteArrayInputStream(resizedImageData));
        assertEquals(250, resizedImage.getWidth());
        assertEquals(250, resizedImage.getHeight());
    }

    @Test
    void testGetDefaultImage() throws IOException {
        byte[] defaultImageData = Utils.getDefaultImage();

        assertNotNull(defaultImageData);
        BufferedImage defaultImage = ImageIO.read(new ByteArrayInputStream(defaultImageData));
        assertEquals(250, defaultImage.getWidth());
        assertEquals(250, defaultImage.getHeight());
    }

    @Test
    void testMapToDTO() {
        // Create a test CPU entity
        CPU cpu = new CPU();
        cpu.setId(1L);
        cpu.setName("Test CPU");
        cpu.setManufacturer("Test Manufacturer");
        cpu.setSocket("AM4");
        cpu.setCores(8);

        // Map to DTO
        CPUDTO cpuDTO = Utils.mapToDTO(cpu, CPUDTO.class);

        assertNotNull(cpuDTO);
        assertEquals(1L, cpuDTO.getId());
        assertEquals("Test CPU", cpuDTO.getName());
        assertEquals("Test Manufacturer", cpuDTO.getManufacturer());
        assertEquals("AM4", cpuDTO.getSocket());
        assertEquals(8, cpuDTO.getCores());
    }

    @Test
    void testMapToEntity() {
        // Create a test CPU DTO
        CPUDTO cpuDTO = new CPUDTO();
        cpuDTO.setId(1L);
        cpuDTO.setName("Test CPU");
        cpuDTO.setManufacturer("Test Manufacturer");
        cpuDTO.setSocket("AM4");
        cpuDTO.setCores(8);

        // Map to entity
        CPU cpu = Utils.mapToEntity(cpuDTO, CPU.class);

        assertNotNull(cpu);
        assertEquals(1L, cpu.getId());
        assertEquals("Test CPU", cpu.getName());
        assertEquals("Test Manufacturer", cpu.getManufacturer());
        assertEquals("AM4", cpu.getSocket());
        assertEquals(8, cpu.getCores());
    }

    @Test
    void testMapToEntityThrowsException() {
        class EntityWithoutNoArgConstructor {
            private final String name;

            public EntityWithoutNoArgConstructor(String name) {
                this.name = name;
            }
        }

        CPUDTO cpuDTO = new CPUDTO();

        assertThrows(RuntimeException.class, () -> {
            Utils.mapToEntity(cpuDTO, EntityWithoutNoArgConstructor.class);
        });
    }
}