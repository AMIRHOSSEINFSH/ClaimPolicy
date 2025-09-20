package org.riton.fetchdbconnector.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.riton.fetchdbconnector.enums.ClaimStatus;
import org.riton.fetchdbconnector.enums.DamageType;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.riton.fetchdbconnector.enums.ClaimStatus.PROCESSING;


@Entity
@Table(name = "claim")
public class ClaimEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private LocalDateTime accidentDate;
    private String accidentLocation;
    private DamageType damageType;
    private String damageDescription;

    private ClaimStatus claimStatus = PROCESSING;

    @Lob
    @Column(name = "image_data", length = 10000000)
    private byte[] imageData;

    @OneToOne
    private PolicyEntity policyEntity;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public PolicyEntity getPolicyEntity() {
        return policyEntity;
    }

    public void setPolicyEntity(PolicyEntity policyEntity) {
        this.policyEntity = policyEntity;
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
