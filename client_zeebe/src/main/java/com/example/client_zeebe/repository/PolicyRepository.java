package com.example.client_zeebe.repository;

import com.example.client_zeebe.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PolicyRepository extends JpaRepository<PolicyEntity, UUID> {



}
