package build.master.repositories;

import build.master.model.entities.Motherboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotherboardRepository extends JpaRepository<Motherboard, Long>, JpaSpecificationExecutor<Motherboard> {
    Page<Motherboard> findBySocket(String socket, Pageable pageable);
    Page<Motherboard> findByChipset(String chipset, Pageable pageable);
    Page<Motherboard> findByFormFactor(String formFactor, Pageable pageable);
    Page<Motherboard> findByMemoryType(String memoryType, Pageable pageable);
    Page<Motherboard> findByManufacturer(String manufacturer, Pageable pageable);

    @Query("SELECT DISTINCT m.manufacturer FROM Motherboard m")
    List<String> findDistinctManufacturers();
}