package build.master.util;

import org.imgscalr.Scalr;
import org.springframework.beans.BeanUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Utils {

    private static final int DEFAULT_IMAGE_WIDTH = 250;
    private static final int DEFAULT_IMAGE_HEIGHT = 250;

    // Resize image to a specific size
    public static byte[] resizeImage(byte[] imageBytes) {
        try {
            // Convert byte[] to BufferedImage
            BufferedImage originalImage = ImageIO.read(new java.io.ByteArrayInputStream(imageBytes));

            // Resize the image to the specified dimensions
            BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);

            // Convert the resized image back to byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Return null in case of an error
        }
    }

    // Generate a default image (white square with "No Image" text)
    public static byte[] getDefaultImage() {
        try {
            // Create a simple default image (e.g., a white square)
            BufferedImage defaultImage = new BufferedImage(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g = defaultImage.createGraphics();
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(0, 0, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
            g.setColor(java.awt.Color.GRAY);
            g.drawString("No Image", DEFAULT_IMAGE_WIDTH / 4, DEFAULT_IMAGE_HEIGHT / 2);
            g.dispose();

            // Convert the default image to byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(defaultImage, "jpg", baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Generic method to map from entity to DTO
    public static <E, D> D mapToDTO(E entity, Class<D> dtoClass) {
        try {
            D dto = dtoClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping entity to DTO", e);
        }
    }

    // Generic method to map from DTO to entity
    public static <E, D> E mapToEntity(D dto, Class<E> entityClass) {
        try {
            E entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(dto, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping DTO to entity", e);
        }
    }
}