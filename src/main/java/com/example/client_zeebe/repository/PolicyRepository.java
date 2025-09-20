package com.example.clientZeebe.repository;

import com.example.clientZeebe.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PolicyRepository extends JpaRepository<PolicyEntity, UUID> {



}
