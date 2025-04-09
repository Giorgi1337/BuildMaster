package build.master.controller;

import build.master.dto.CPUDTO;
import build.master.model.entities.CPU;
import build.master.service.CPUService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cpus")
public class CPUController {

    private final CPUService cpuService;

    public CPUController(CPUService cpuService) {
        this.cpuService = cpuService;
    }

    @GetMapping
    public String listCPUs(@RequestParam(required = false) String socket,
                           @RequestParam(required = false) String manufacturer,
                           @RequestParam(required = false) Integer cores,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "9") int size,
                           @RequestParam(defaultValue = "name") String sortBy,
                           @RequestParam(defaultValue = "asc") String sortOrder,
                           Model model) {

        Sort sort = Sort.by(Sort.Order.asc(sortBy));
        if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = Sort.by(Sort.Order.desc(sortBy));
        }
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CPU> cpuPage;

        if (socket != null && !socket.isEmpty()) {
            cpuPage = cpuService.getCPUsBySocket(socket, pageable);
        } else if (manufacturer != null && !manufacturer.isEmpty()) {
            cpuPage = cpuService.getCPUsByManufacturer(manufacturer, pageable);
        } else if (cores != null) {
            cpuPage = cpuService.getCPUsByCores(cores, pageable);
        } else {
            cpuPage = cpuService.getAllCPUs(pageable);
        }

        List<String> availableManufacturers = cpuService.getAllManufacturers();

        model.addAttribute("cpus", cpuPage.getContent());
        model.addAttribute("availableManufacturers", availableManufacturers);
        model.addAttribute("totalPages", cpuPage.getTotalPages());
        model.addAttribute("totalItems", cpuPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "cpu/cpus";
    }

    @GetMapping("/add")
    public String showAddCPUForm(Model model) {
        model.addAttribute("cpuDTO", new CPUDTO());
        return "cpu/add-cpu";
    }

    @PostMapping("/add")
    public String addCPU(@ModelAttribute @Valid CPUDTO cpuDTO,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cpuDTO", cpuDTO);
            return "cpu/add-cpu";
        }

        try {
            CPU cpu = cpuService.mapToEntity(cpuDTO);

            // Handle image upload
            if (cpuDTO.getImage() != null && !cpuDTO.getImage().isEmpty()) {
                try {
                    cpu.setImage(cpuDTO.getImage().getBytes());
                } catch (IOException e) {
                    model.addAttribute("errorMessage", "Error processing image: " + e.getMessage());
                    return "cpu/add-cpu";
                }
            }

            cpuService.saveCPU(cpu);
            return "redirect:/cpus";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error saving CPU: " + e.getMessage());
            return "cpu/add-cpu";
        }
    }

    @GetMapping("/{id}")
    public String viewCPUDetails(@PathVariable("id") Long id, Model model) {
        Optional<CPU> cpuOptional = cpuService.getCPUById(id);

        if (cpuOptional.isEmpty()) {
            return "redirect:/cpus";
        }

        CPU cpu = cpuOptional.get();

        if (cpu.getIntegratedGraphics() == null) {
            cpu.setIntegratedGraphics(false);
        }

        model.addAttribute("cpu", cpu);
        return "cpu/cpu-details";
    }

    @GetMapping("/edit/{id}")
    public String showEditCPUForm(@PathVariable("id") Long id, Model model) {
        Optional<CPU> cpuOptional = cpuService.getCPUById(id);

        if (cpuOptional.isEmpty()) {
            return "redirect:/cpus";
        }

        CPUDTO cpuDTO = cpuService.mapToDTO(cpuOptional.get());

        if (cpuDTO == null) {
            throw new IllegalStateException("CPUDTO is null for CPU ID: " + id);
        }

        if (cpuDTO.getId() == null) {
            throw new IllegalStateException("CPUDTO ID is null for CPU ID: " + id);
        }

        model.addAttribute("cpuDTO", cpuDTO);
        model.addAttribute("cpuId", id);

        return "cpu/edit-cpu";
    }

    @PostMapping("/edit/{id}")
    public String updateCPU(@PathVariable("id") Long id,
                            @ModelAttribute @Valid CPUDTO cpuDTO,
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cpuDTO", cpuDTO);
            model.addAttribute("cpuId", id);
            return "cpu/edit-cpu";
        }

        try {
            CPU updatedCPU = cpuService.updateCPU(id, cpuDTO);
            if (cpuDTO.getImage() != null && !cpuDTO.getImage().isEmpty()) {
                updatedCPU.setImage(cpuDTO.getImage().getBytes());
                cpuService.saveCPU(updatedCPU);
            }

            return "redirect:/cpus";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error processing image: " + e.getMessage());
            return "cpu/edit-cpu";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "cpu/edit-cpu";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating CPU: " + e.getMessage());
            return "cpu/edit-cpu";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCpu(@PathVariable Long id, Model model) {
        cpuService.deleteCPU(id);
        model.addAttribute("message", "CPU successfully deleted.");
        return "redirect:/cpus";
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getCPUImage(@PathVariable("id") Long id) {
        Optional<CPU> cpuOptional = cpuService.getCPUById(id);

        if (cpuOptional.isPresent() && cpuOptional.get().getImage() != null) {
            byte[] imageData = cpuOptional.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageData.length);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }
}

