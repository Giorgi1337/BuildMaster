package build.master.service.impl;

import build.master.dto.MotherboardDTO;
import build.master.model.entities.Motherboard;
import build.master.repositories.MotherboardRepository;
import build.master.service.MotherboardService;
import build.master.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MotherboardServiceImpl implements MotherboardService {

    private final MotherboardRepository motherboardRepository;

    @Autowired
    public MotherboardServiceImpl(MotherboardRepository motherboardRepository) {
        this.motherboardRepository = motherboardRepository;
    }

    @Override
    public Page<Motherboard> getAllMotherboards(Pageable pageable) {
        return motherboardRepository.findAll(pageable);
    }

    @Override
    public Page<Motherboard> getAllMotherboards(String socket, String chipset, String manufacturer, Pageable pageable) {
        Specification<Motherboard> spec = Specification.where(null);

        if (socket != null && !socket.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("socket"), socket));
        }

        if (chipset != null && !chipset.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("chipset"), chipset));
        }

        if (manufacturer != null && !manufacturer.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("manufacturer"), manufacturer));
        }

        return motherboardRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<Motherboard> getMotherboardById(Long id) {
        return motherboardRepository.findById(id);
    }

    @Override
    public Page<Motherboard> getMotherboardsBySocket(String socket, Pageable pageable) {
        return motherboardRepository.findBySocket(socket, pageable);
    }

    @Override
    public Page<Motherboard> getMotherboardsByChipset(String chipset, Pageable pageable) {
        return motherboardRepository.findByChipset(chipset, pageable);
    }

    @Override
    public Page<Motherboard> getMotherboardsByFormFactor(String formFactor, Pageable pageable) {
        return motherboardRepository.findByFormFactor(formFactor, pageable);
    }

    @Override
    public Page<Motherboard> getMotherboardsByMemoryType(String memoryType, Pageable pageable) {
        return motherboardRepository.findByMemoryType(memoryType, pageable);
    }

    @Override
    public Page<Motherboard> getMotherboardsByManufacturer(String manufacturer, Pageable pageable) {
        return motherboardRepository.findByManufacturer(manufacturer, pageable);
    }

    @Override
    public List<String> getAllManufacturers() {
        return motherboardRepository.findDistinctManufacturers();
    }

    @Override
    @Transactional
    public Motherboard mapToEntity(MotherboardDTO dto) {
        // Use the generic mapper method first
        Motherboard motherboard = Utils.mapToEntity(dto, Motherboard.class);

        // Handle the image separately since it requires special processing
        MultipartFile imageFile = dto.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                motherboard.setImage(imageFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image upload", e);
            }
        }

        return motherboard;
    }

    @Override
    public MotherboardDTO mapToDTO(Motherboard motherboard) {
        // Use the generic mapper method
        MotherboardDTO dto = Utils.mapToDTO(motherboard, MotherboardDTO.class);

        // Note: Image is intentionally not mapped to DTO since MultipartFile is for upload only
        return dto;
    }

    @Override
    @Transactional
    public Motherboard saveMotherboard(Motherboard motherboard) {
        return motherboardRepository.save(motherboard);
    }

    @Override
    @Transactional
    public Motherboard updateMotherboard(Long id, Motherboard motherboard) {
        Optional<Motherboard> existingMotherboardOpt = motherboardRepository.findById(id);

        if (existingMotherboardOpt.isPresent()) {
            Motherboard existingMotherboard = existingMotherboardOpt.get();

            // Store the current image
            byte[] currentImage = existingMotherboard.getImage();

            // Copy all properties from the updated motherboard to the existing one
            existingMotherboard.setName(motherboard.getName());
            existingMotherboard.setManufacturer(motherboard.getManufacturer());
            existingMotherboard.setModel(motherboard.getModel());
            existingMotherboard.setPrice(motherboard.getPrice());
            existingMotherboard.setSocket(motherboard.getSocket());
            existingMotherboard.setChipset(motherboard.getChipset());
            existingMotherboard.setFormFactor(motherboard.getFormFactor());
            existingMotherboard.setMemoryType(motherboard.getMemoryType());
            existingMotherboard.setMemorySlots(motherboard.getMemorySlots());
            existingMotherboard.setMaxMemory(motherboard.getMaxMemory());
            existingMotherboard.setSataConnectors(motherboard.getSataConnectors());
            existingMotherboard.setM2Slots(motherboard.getM2Slots());
            existingMotherboard.setPciExpressSlots(motherboard.getPciExpressSlots());
            existingMotherboard.setWifi(motherboard.getWifi());
            existingMotherboard.setBluetooth(motherboard.getBluetooth());

            // Only update image if new one is provided
            if (motherboard.getImage() != null && motherboard.getImage().length > 0) {
                existingMotherboard.setImage(motherboard.getImage());
            } else {
                existingMotherboard.setImage(currentImage);
            }

            return motherboardRepository.save(existingMotherboard);
        } else {
            throw new RuntimeException("Motherboard not found with ID: " + id);
        }
    }

    @Override
    @Transactional
    public Motherboard updateMotherboard(Long id, MotherboardDTO motherboardDTO) {
        Optional<Motherboard> existingMotherboardOpt = motherboardRepository.findById(id);

        if (existingMotherboardOpt.isPresent()) {
            Motherboard existingMotherboard = existingMotherboardOpt.get();

            // Store the current image
            byte[] currentImage = existingMotherboard.getImage();

            // Map DTO to Entity
            Motherboard updatedMotherboard = mapToEntity(motherboardDTO);
            updatedMotherboard.setId(id); // Ensure ID is preserved

            // Handle the image separately
            MultipartFile imageFile = motherboardDTO.getImage();
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    updatedMotherboard.setImage(imageFile.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to process image upload", e);
                }
            } else {
                updatedMotherboard.setImage(currentImage);
            }

            return motherboardRepository.save(updatedMotherboard);
        } else {
            throw new RuntimeException("Motherboard not found with ID: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteMotherboard(Long id) {
        motherboardRepository.deleteById(id);
    }
}