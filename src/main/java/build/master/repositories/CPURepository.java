package build.master.repositories;

import build.master.model.entities.CPU;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CPURepository extends JpaRepository<CPU, Long> {
    Page<CPU> findBySocket(String socket, Pageable pageable);
    Page<CPU> findByManufacturer(String manufacturer, Pageable pageable);
    Page<CPU> findByCoresGreaterThanEqual(Integer cores, Pageable pageable);

    @Query("SELECT DISTINCT c.manufacturer FROM CPU c ORDER BY c.manufacturer")
    List<String> findDistinctManufacturers();
}