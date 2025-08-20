package com.example.demo.common.drools.dto;

import java.time.LocalDateTime;

public class CleanupResult {
    private Long deletedCount;
    private LocalDateTime deletedBefore;
    private Long remainingCount;

    public Long getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(Long deletedCount) {
        this.deletedCount = deletedCount;
    }

    public LocalDateTime getDeletedBefore() {
        return deletedBefore;
    }

    public void setDeletedBefore(LocalDateTime deletedBefore) {
        this.deletedBefore = deletedBefore;
    }

    public Long getRemainingCount() {
        return remainingCount;
    }

    public void setRemainingCount(Long remainingCount) {
        this.remainingCount = remainingCount;
    }
}