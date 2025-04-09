package build.master.data;

import build.master.model.entities.CPU;
import build.master.model.entities.Motherboard;
import build.master.repositories.CPURepository;
import build.master.repositories.MotherboardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CPURepository cpuRepository;
    private final MotherboardRepository motherboardRepository;
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(CPURepository cpuRepository, MotherboardRepository motherboardRepository) {
        this.cpuRepository = cpuRepository;
        this.motherboardRepository = motherboardRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (cpuRepository.count() == 0) {
            List<CPU> cpus = new ArrayList<>(List.of(
                    new CPU(null, "Intel Core i9-11900K", "Intel", "i9-11900K", 799.99, "LGA1200", 8, 16, 3.5, 5.3, 125, "x86_64", true, null),
                    new CPU(null, "AMD Ryzen 9 5900X", "AMD", "Ryzen 9 5900X", 749.99, "AM4", 12, 24, 3.7, 4.8, 105, "x86_64", true, null),
                    new CPU(null, "Intel Core i9-13900K", "Intel", "i9-13900K", 799.99, "LGA1700", 24, 32, 3.0, 5.8, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i7-13700K", "Intel", "i7-13700K", 549.99, "LGA1700", 16, 24, 3.4, 5.4, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i5-13600K", "Intel", "i5-13600K", 349.99, "LGA1700", 14, 20, 3.5, 5.1, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i9-14900K", "Intel", "i9-14900K", 899.99, "LGA1700", 24, 32, 3.2, 6.0, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i7-14700K", "Intel", "i7-14700K", 649.99, "LGA1700", 16, 24, 3.5, 5.5, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i5-14600K", "Intel", "i5-14600K", 399.99, "LGA1700", 12, 16, 3.6, 5.3, 125, "x86_64", true, null),

                    new CPU(null, "Intel Core i9-13800K", "Intel", "i9-13800K", 749.99, "LGA1700", 24, 32, 3.0, 5.6, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i7-13600K", "Intel", "i7-13600K", 499.99, "LGA1700", 16, 24, 3.3, 5.2, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i5-13500K", "Intel", "i5-13500K", 349.99, "LGA1700", 12, 16, 3.5, 5.1, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i9-12900KF", "Intel", "i9-12900KF", 799.99, "LGA1700", 16, 24, 3.2, 5.2, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i7-12700K", "Intel", "i7-12700K", 449.99, "LGA1700", 12, 20, 3.6, 5.0, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i5-12600KF", "Intel", "i5-12600KF", 349.99, "LGA1700", 10, 16, 3.7, 5.1, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i3-12100F", "Intel", "i3-12100F", 149.99, "LGA1700", 4, 8, 3.3, 4.3, 65, "x86_64", true, null),
                    new CPU(null, "Intel Core i5-12400F", "Intel", "i5-12400F", 219.99, "LGA1700", 6, 12, 2.5, 4.4, 65, "x86_64", true, null),
                    new CPU(null, "Intel Core i3-12300", "Intel", "i3-12300", 169.99, "LGA1700", 4, 8, 3.4, 4.5, 65, "x86_64", true, null),
                    new CPU(null, "Intel Core i7-11700KF", "Intel", "i7-11700KF", 399.99, "LGA1200", 8, 16, 3.6, 5.1, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i9-11900KF", "Intel", "i9-11900KF", 699.99, "LGA1200", 8, 16, 3.5, 5.3, 125, "x86_64", true, null),
                    new CPU(null, "Intel Core i5-11400", "Intel", "i5-11400", 229.99, "LGA1200", 6, 12, 2.6, 4.4, 65, "x86_64", true, null),
                    new CPU(null, "Intel Core i3-10320", "Intel", "i3-10320", 129.99, "LGA1200", 4, 8, 3.8, 4.6, 62, "x86_64", true, null)
            ));

            cpuRepository.saveAll(cpus);
            logger.info("CPU data inserted in batch.");
        }

        if (motherboardRepository.count() == 0) {
            List<Motherboard> motherboards = new ArrayList<>(List.of(
                    new Motherboard(null, "ASUS ROG Strix Z590-E", "ASUS", "ROG Strix Z590-E", 299.99, null, "LGA1200", "Intel Z590", "ATX", "DDR4", 4, 128, 6, 2, 3, true, true),
                    new Motherboard(null, "MSI MAG B550 TOMAHAWK", "MSI", "MAG B550 TOMAHAWK", 139.99, null, "AM4", "AMD B550", "ATX", "DDR4", 4, 64, 6, 2, 2, false, true),
                    new Motherboard(null, "ASUS ROG Strix Z790-E", "ASUS", "ROG Strix Z790-E", 349.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 4, true, true),
                    new Motherboard(null, "MSI MPG Z790 EDGE WIFI", "MSI", "MPG Z790 EDGE WIFI", 319.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 4, true, true),
                    new Motherboard(null, "Gigabyte Z790 AORUS ELITE", "Gigabyte", "Z790 AORUS ELITE", 299.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 4, true, true),
                    new Motherboard(null, "ASUS Z790-A PRO", "ASUS", "Z790-A PRO", 389.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 4, true, true),
                    new Motherboard(null, "MSI MEG Z790 ACE", "MSI", "MEG Z790 ACE", 449.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 4, true, true),
                    new Motherboard(null, "Gigabyte Z790 AORUS XTREME", "Gigabyte", "Z790 AORUS XTREME", 599.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 6, true, true),
                    new Motherboard(null, "ASRock Z790 Phantom Gaming 4", "ASRock", "Z790 Phantom Gaming 4", 299.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 4, true, true),
                    new Motherboard(null, "MSI MAG Z790 TOMAHAWK WIFI", "MSI", "MAG Z790 TOMAHAWK WIFI", 329.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 4, true, true),
                    new Motherboard(null, "Gigabyte Z790 AORUS PRO", "Gigabyte", "Z790 AORUS PRO", 379.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 5, true, true),
                    new Motherboard(null, "ASUS TUF Z790-PLUS WIFI", "ASUS", "TUF Z790-PLUS WIFI", 269.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 4, true, true),
                    new Motherboard(null, "MSI Z790 PRO WIFI", "MSI", "Z790 PRO WIFI", 249.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 4, true, true),
                    new Motherboard(null, "ASRock Z790 Steel Legend", "ASRock", "Z790 Steel Legend", 319.99, null, "LGA1700", "Intel Z790", "ATX", "DDR5", 4, 128, 6, 2, 5, true, true)
            ));

            motherboardRepository.saveAll(motherboards);
            logger.info("Motherboard data inserted in batch.");
        }
    }
}
