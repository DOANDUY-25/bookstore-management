package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.dto.GiftPackageDTO;
import com.bookshop.bookshop.entity.GiftPackage;
import com.bookshop.bookshop.service.GiftPackageService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/gift-packages")
public class AdminGiftPackageController {
    
    private final GiftPackageService giftPackageService;

    public AdminGiftPackageController(GiftPackageService giftPackageService) {
        this.giftPackageService = giftPackageService;
    }
    
    @GetMapping
    public String listGiftPackages(Model model) {
        List<GiftPackage> packages = giftPackageService.getAllGiftPackages();
        model.addAttribute("packages", packages);
        return "admin/gift-packages/list";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("giftPackage", new GiftPackageDTO());
        return "admin/gift-packages/create";
    }
    
    @PostMapping("/create")
    public String createGiftPackage(@Valid @ModelAttribute("giftPackage") GiftPackageDTO dto,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/gift-packages/create";
        }
        
        try {
            giftPackageService.createGiftPackage(dto);
            redirectAttributes.addFlashAttribute("success", "Gift package created successfully");
            return "redirect:/admin/gift-packages";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/gift-packages/create";
        }
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        GiftPackage giftPackage = giftPackageService.getGiftPackageById(id);
        GiftPackageDTO dto = new GiftPackageDTO();
        dto.setPackageName(giftPackage.getPackageName());
        dto.setGiftBox(giftPackage.getGiftBox());
        dto.setGreetingCard(giftPackage.getGreetingCard());
        dto.setPaperType(giftPackage.getPaperType());
        dto.setDescription(giftPackage.getDescription());
        dto.setPackageFee(giftPackage.getPackageFee());
        dto.setImageURL(giftPackage.getImageURL());
        
        model.addAttribute("giftPackage", dto);
        model.addAttribute("packageId", id);
        return "admin/gift-packages/edit";
    }
    
    @PostMapping("/{id}/edit")
    public String updateGiftPackage(@PathVariable Integer id,
                                   @Valid @ModelAttribute("giftPackage") GiftPackageDTO dto,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (result.hasErrors()) {
            model.addAttribute("packageId", id);
            return "admin/gift-packages/edit";
        }
        
        try {
            giftPackageService.updateGiftPackage(id, dto);
            redirectAttributes.addFlashAttribute("success", "Gift package updated successfully");
            return "redirect:/admin/gift-packages";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/gift-packages/" + id + "/edit";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteGiftPackage(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            giftPackageService.deleteGiftPackage(id);
            redirectAttributes.addFlashAttribute("success", "Gift package deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/gift-packages";
    }
}
