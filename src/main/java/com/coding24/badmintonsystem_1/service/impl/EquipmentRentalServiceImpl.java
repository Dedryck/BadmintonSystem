package com.coding24.badmintonsystem_1.service.impl;

import com.coding24.badmintonsystem_1.entity.EquipmentRental;
import com.coding24.badmintonsystem_1.mapper.EquipmentRentalMapper;
import com.coding24.badmintonsystem_1.service.EquipmentRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentRentalServiceImpl implements EquipmentRentalService {

    private final EquipmentRentalMapper equipmentRentalMapper;

    @Autowired
    public EquipmentRentalServiceImpl(EquipmentRentalMapper equipmentRentalMapper) {
        this.equipmentRentalMapper = equipmentRentalMapper;
    }

    @Override
    public EquipmentRental findById(Integer rentalID) {
        return equipmentRentalMapper.findById(rentalID);
    }

    @Override
    public List<EquipmentRental> findAll() {
        return equipmentRentalMapper.findAll();
    }

    @Override
    public void insertEquipmentRental(EquipmentRental equipmentRental) {
        equipmentRentalMapper.insertEquipmentRental(equipmentRental);
    }

    @Override
    public void updateEquipmentRental(EquipmentRental equipmentRental) {
        equipmentRentalMapper.updateEquipmentRental(equipmentRental);
    }

    @Override
    public void deleteEquipmentRental(Integer rentalID) {
        equipmentRentalMapper.deleteEquipmentRental(rentalID);
    }
}
