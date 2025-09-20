package com.example.client_zeebe.common.drools.repository;

import com.example.client_zeebe.common.drools.entity.RuleExecutionLog;
import com.example.client_zeebe.common.drools.enums.ExecutionStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RuleExecutionLogRepository extends JpaRepository<RuleExecutionLog, UUID> {
    
    List<RuleExecutionLog> findByProcessInstanceId(String processInstanceId);
    
    Page<RuleExecutionLog> findByExecutedAtBetween(
            LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    @Query("SELECT l FROM RuleExecutionLog l WHERE l.status = :status")
    List<RuleExecutionLog> findByStatus(@Param("status") ExecutionStatus status);

    @Query(value = "SELECT * FROM rule_execution_log l WHERE l.categories_executed @> cast(:categoryJson AS jsonb)", nativeQuery = true)
    List<RuleExecutionLog> findByCategoryExecutedNative(@Param("categoryJson") String categoryJson);

    @Query("SELECT COUNT(l), AVG(l.executionTimeMs), MAX(l.executionTimeMs), MIN(l.executionTimeMs) " +
           "FROM RuleExecutionLog l " +
           "WHERE l.executedAt BETWEEN :start AND :end")
    Object[] getExecutionStatistics(@Param("start") LocalDateTime start, 
                                   @Param("end") LocalDateTime end);
    
    @Query("SELECT l.status, COUNT(l) FROM RuleExecutionLog l " +
           "WHERE l.executedAt BETWEEN :start AND :end " +
           "GROUP BY l.status")
    List<Object[]> getStatusDistribution(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM RuleExecutionLog l WHERE l.executedAt < :before")
    void deleteLogsOlderThan(@Param("before") LocalDateTime before);
}
