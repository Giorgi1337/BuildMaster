package build.master.controller;

import build.master.dto.MotherboardDTO;
import build.master.model.entities.Motherboard;
import build.master.service.MotherboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/motherboards")
public class MotherboardController {

    private final MotherboardService motherboardService;

    @Autowired
    public MotherboardController(MotherboardService motherboardService) {
        this.motherboardService = motherboardService;
    }

    @GetMapping
    public String getAllMotherboards(
            @RequestParam(required = false) String socket,
            @RequestParam(required = false) String chipset,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Motherboard> motherboardPage;

        if (socket != null || chipset != null || manufacturer != null) {
            motherboardPage = motherboardService.getAllMotherboards(socket, chipset, manufacturer, pageable);
        } else {
            motherboardPage = motherboardService.getAllMotherboards(pageable);
        }

        List<String> manufacturers = motherboardService.getAllManufacturers();

        model.addAttribute("motherboards", motherboardPage.getContent());
        model.addAttribute("currentPage", motherboardPage.getNumber());
        model.addAttribute("totalPages", motherboardPage.getTotalPages());
        model.addAttribute("totalItems", motherboardPage.getTotalElements());
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("socket", socket);
        model.addAttribute("chipset", chipset);
        model.addAttribute("manufacturer", manufacturer);
        model.addAttribute("sort", sort);

        return "motherboard/list";
    }

    @GetMapping("/{id}")
    public String getMotherboardById(@PathVariable Long id, Model model) {
        Optional<Motherboard> motherboard = motherboardService.getMotherboardById(id);

        if (motherboard.isPresent()) {
            model.addAttribute("motherboard", motherboard.get());
            return "motherboard/motherboard-detail";
        } else {
            return "redirect:/motherboards";
        }
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("motherboardDTO", new MotherboardDTO());
        model.addAttribute("manufacturers", motherboardService.getAllManufacturers());
        return "motherboard/add-motherboard";
    }

    @PostMapping("/new")
    public String createMotherboard(@ModelAttribute MotherboardDTO motherboardDTO,
                                    RedirectAttributes redirectAttributes) {
        try {
            Motherboard motherboard = motherboardService.mapToEntity(motherboardDTO);
            motherboardService.saveMotherboard(motherboard);
            redirectAttributes.addFlashAttribute("success", "Motherboard created successfully!");
            return "redirect:/motherboards";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating motherboard: " + e.getMessage());
            return "redirect:/motherboards/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Motherboard> motherboardOpt = motherboardService.getMotherboardById(id);

        if (motherboardOpt.isPresent()) {
            Motherboard motherboard = motherboardOpt.get();
            MotherboardDTO motherboardDTO = motherboardService.mapToDTO(motherboard);
            model.addAttribute("motherboardDTO", motherboardDTO);
            model.addAttribute("motherboardId", id);
            model.addAttribute("manufacturers", motherboardService.getAllManufacturers());
            return "motherboard/edit-motherboard";
        } else {
            return "redirect:/motherboards";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateMotherboard(@PathVariable Long id,
                                    @ModelAttribute MotherboardDTO motherboardDTO,
                                    RedirectAttributes redirectAttributes) {
        try {
            motherboardService.updateMotherboard(id, motherboardDTO);
            redirectAttributes.addFlashAttribute("success", "Motherboard updated successfully!");
            return "redirect:/motherboards/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating motherboard: " + e.getMessage());
            return "redirect:/motherboards/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteMotherboard(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            motherboardService.deleteMotherboard(id);
            redirectAttributes.addFlashAttribute("success", "Motherboard deleted successfully!");
            return "redirect:/motherboards";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting motherboard: " + e.getMessage());
            return "redirect:/motherboards";
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getMotherboardImage(@PathVariable Long id) {
        Optional<Motherboard> motherboardOpt = motherboardService.getMotherboardById(id);

        if (motherboardOpt.isPresent() && motherboardOpt.get().getImage() != null) {
            Motherboard motherboard = motherboardOpt.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(motherboard.getImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}