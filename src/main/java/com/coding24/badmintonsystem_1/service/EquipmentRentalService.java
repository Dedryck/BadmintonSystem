package com.coding24.badmintonsystem_1.service;

import com.coding24.badmintonsystem_1.entity.EquipmentRental;

import java.util.List;

public interface EquipmentRentalService {
    EquipmentRental findById(Integer rentalID);
    List<EquipmentRental> findAll();
    void insertEquipmentRental(EquipmentRental equipmentRental);
    void updateEquipmentRental(EquipmentRental equipmentRental);
    void deleteEquipmentRental(Integer rentalID);
}
