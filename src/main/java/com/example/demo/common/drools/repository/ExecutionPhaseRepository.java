package com.example.demo.common.drools.repository;

import com.example.demo.common.drools.entity.ExecutionPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExecutionPhaseRepository extends JpaRepository<ExecutionPhase, UUID> {
    
    List<ExecutionPhase> findByExecutionPlan_Id(UUID planId);
    
    @Query("SELECT p FROM ExecutionPhase p JOIN FETCH p.categories WHERE p.executionPlan.id = :planId")
    List<ExecutionPhase> findByPlanIdWithCategories(@Param("planId") UUID planId);
    
    @Query("SELECT p FROM ExecutionPhase p WHERE :categoryId MEMBER OF p.categories")
    List<ExecutionPhase> findByCategoryId(@Param("categoryId") UUID categoryId);
}