package com.example.demo.common.drools.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateCategoryRequest {
    
    @NotBlank(message = "Category code is required")
    @Pattern(regexp = "^[A-Z][A-Z0-9_]*$", message = "Category code must be uppercase with underscores")
    private String categoryCode;
    
    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 255)
    private String categoryName;
    
    private String description;
    
    private String icon;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color")
    private String color;
    
    private Integer sortOrder = 0;
    
    private UUID parentCategoryId;
    
    private Map<String, Object> metadata = new HashMap<>();
    
    private boolean active = true;

    public @NotBlank(message = "Category code is required") @Pattern(regexp = "^[A-Z][A-Z0-9_]*$", message = "Category code must be uppercase with underscores") String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(@NotBlank(message = "Category code is required") @Pattern(regexp = "^[A-Z][A-Z0-9_]*$", message = "Category code must be uppercase with underscores") String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public @NotBlank(message = "Category name is required") @Size(min = 3, max = 255) String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(@NotBlank(message = "Category name is required") @Size(min = 3, max = 255) String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color") String getColor() {
        return color;
    }

    public void setColor(@Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color") String color) {
        this.color = color;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public UUID getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(UUID parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
