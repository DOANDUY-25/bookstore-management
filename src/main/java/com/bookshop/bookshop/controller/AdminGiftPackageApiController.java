package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.dto.GiftPackageDTO;
import com.bookshop.bookshop.entity.GiftPackage;
import com.bookshop.bookshop.service.GiftPackageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/gift-packages")
public class AdminGiftPackageApiController {
    
    private final GiftPackageService giftPackageService;

    public AdminGiftPackageApiController(GiftPackageService giftPackageService) {
        this.giftPackageService = giftPackageService;
    }
    
    @GetMapping
    public ResponseEntity<List<GiftPackage>> getAllGiftPackages() {
        List<GiftPackage> packages = giftPackageService.getAllGiftPackages();
        return ResponseEntity.ok(packages);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GiftPackage> getGiftPackageById(@PathVariable Integer id) {
        GiftPackage giftPackage = giftPackageService.getGiftPackageById(id);
        return ResponseEntity.ok(giftPackage);
    }
    
    @PostMapping
    public ResponseEntity<GiftPackage> createGiftPackage(@Valid @RequestBody GiftPackageDTO dto) {
        GiftPackage created = giftPackageService.createGiftPackage(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GiftPackage> updateGiftPackage(@PathVariable Integer id,
                                                         @Valid @RequestBody GiftPackageDTO dto) {
        GiftPackage updated = giftPackageService.updateGiftPackage(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftPackage(@PathVariable Integer id) {
        giftPackageService.deleteGiftPackage(id);
        return ResponseEntity.noContent().build();
    }
}
