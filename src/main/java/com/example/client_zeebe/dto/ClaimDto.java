package com.example.clientZeebe.dto;

import com.example.clientZeebe.common.enums.DamageType;
import com.example.clientZeebe.entity.PolicyEntity;
import com.example.clientZeebe.entity.VehicleEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClaimDto {

    private UUID policyId;
    private LocalDateTime accidentDate;
    private String accidentLocation;
    private DamageType damageType;
    private String damageDescription;
    private UUID vehicleId;


    private VehicleEntity vehicle;


    private PolicyEntity policy;

    public UUID getPolicyId() {
        return policyId;
    }

    public void setPolicyId(UUID policyId) {
        this.policyId = policyId;
    }

    public LocalDateTime getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(LocalDateTime accidentDate) {
        this.accidentDate = accidentDate;
    }

    public String getAccidentLocation() {
        return accidentLocation;
    }

    public void setAccidentLocation(String accidentLocation) {
        this.accidentLocation = accidentLocation;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    public PolicyEntity getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyEntity policy) {
        this.policy = policy;
    }
}


