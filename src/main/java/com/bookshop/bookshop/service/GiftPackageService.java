package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.GiftPackageDTO;
import com.bookshop.bookshop.entity.GiftPackage;

import java.util.List;

public interface GiftPackageService {
    
    List<GiftPackage> getAllGiftPackages();
    
    GiftPackage getGiftPackageById(Integer giftPackageId);
    
    GiftPackage createGiftPackage(GiftPackageDTO dto);
    
    GiftPackage updateGiftPackage(Integer giftPackageId, GiftPackageDTO dto);
    
    void deleteGiftPackage(Integer giftPackageId);
}
