package com.example.client_zeebe.common.drools.repository;
import com.example.client_zeebe.common.drools.entity.ExecutionPlan;
import com.example.client_zeebe.common.drools.enums.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface ExecutionPlanRepository extends JpaRepository<ExecutionPlan, UUID> {
    
    Optional<ExecutionPlan> findByPlanName(String planName);
    
    List<ExecutionPlan> findByActiveTrue();
    
    @Query("SELECT p FROM ExecutionPlan p LEFT JOIN FETCH p.phases WHERE p.id = :id")
    Optional<ExecutionPlan> findByIdWithPhases(@Param("id") UUID id);
    
    @Query("SELECT p FROM ExecutionPlan p WHERE p.planType = :type AND p.active = true")
    List<ExecutionPlan> findByPlanType(@Param("type") PlanType type);
    
    @Modifying
    @Query("UPDATE ExecutionPlan p SET p.active = :active WHERE p.id = :id")
    void updateActiveStatus(@Param("id") UUID id, @Param("active") boolean active);
}