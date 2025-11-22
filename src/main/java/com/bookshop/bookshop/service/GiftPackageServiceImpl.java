package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.GiftPackageDTO;
import com.bookshop.bookshop.entity.GiftPackage;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.repository.GiftPackageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GiftPackageServiceImpl implements GiftPackageService {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GiftPackageServiceImpl.class);
    private final GiftPackageRepository giftPackageRepository;

    public GiftPackageServiceImpl(GiftPackageRepository giftPackageRepository) {
        this.giftPackageRepository = giftPackageRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GiftPackage> getAllGiftPackages() {
        return giftPackageRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public GiftPackage getGiftPackageById(Integer giftPackageId) {
        return giftPackageRepository.findById(giftPackageId)
                .orElseThrow(() -> new ResourceNotFoundException("GiftPackage", "id", giftPackageId));
    }
    
    @Override
    public GiftPackage createGiftPackage(GiftPackageDTO dto) {
        log.info("Creating gift package: {}", dto.getPackageName());
        
        GiftPackage giftPackage = new GiftPackage();
        giftPackage.setPackageName(dto.getPackageName());
        giftPackage.setGiftBox(dto.getGiftBox());
        giftPackage.setGreetingCard(dto.getGreetingCard());
        giftPackage.setPaperType(dto.getPaperType());
        giftPackage.setDescription(dto.getDescription());
        giftPackage.setPackageFee(dto.getPackageFee());
        giftPackage.setImageURL(dto.getImageURL());
        
        return giftPackageRepository.save(giftPackage);
    }
    
    @Override
    public GiftPackage updateGiftPackage(Integer giftPackageId, GiftPackageDTO dto) {
        log.info("Updating gift package: {}", giftPackageId);
        
        GiftPackage giftPackage = giftPackageRepository.findById(giftPackageId)
                .orElseThrow(() -> new ResourceNotFoundException("GiftPackage", "id", giftPackageId));
        
        giftPackage.setPackageName(dto.getPackageName());
        giftPackage.setGiftBox(dto.getGiftBox());
        giftPackage.setGreetingCard(dto.getGreetingCard());
        giftPackage.setPaperType(dto.getPaperType());
        giftPackage.setDescription(dto.getDescription());
        giftPackage.setPackageFee(dto.getPackageFee());
        giftPackage.setImageURL(dto.getImageURL());
        
        return giftPackageRepository.save(giftPackage);
    }
    
    @Override
    public void deleteGiftPackage(Integer giftPackageId) {
        log.info("Deleting gift package: {}", giftPackageId);
        
        GiftPackage giftPackage = giftPackageRepository.findById(giftPackageId)
                .orElseThrow(() -> new ResourceNotFoundException("GiftPackage", "id", giftPackageId));
        
        giftPackageRepository.delete(giftPackage);
    }
}
