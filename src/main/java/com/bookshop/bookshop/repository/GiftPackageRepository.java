package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.entity.GiftPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftPackageRepository extends JpaRepository<GiftPackage, Integer> {
    // Standard CRUD operations provided by JpaRepository
}
