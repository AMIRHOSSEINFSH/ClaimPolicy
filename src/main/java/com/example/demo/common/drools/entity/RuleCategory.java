package com.example.demo.common.drools.entity;
import com.example.demo.common.drools.JsonbConverter;
import com.example.demo.common.drools.enums.RuleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;
@Entity
@Table(name = "rule_categories", indexes = {
    @Index(name = "idx_category_code", columnList = "category_code", unique = true),
    @Index(name = "idx_parent_category", columnList = "parent_category_id")
})
@EntityListeners(AuditingEntityListener.class)
public class RuleCategory {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "category_code", unique = true, nullable = false, length = 100)
    @NotBlank(message = "Category code is required")
    private String categoryCode;
    
    @Column(name = "category_name", length = 255)
    @NotBlank(message = "Category name is required")
    private String categoryName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "icon", length = 50)
    private String icon;
    
    @Column(name = "color", length = 7)
    private String color;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    // Self-referencing for hierarchy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
//    @ToString.Exclude
    private RuleCategory parentCategory;
    
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
    private Set<RuleCategory> subCategories = new HashSet<>();
    
    // Many-to-Many relationship with rules
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "rule_category_mappings",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "rule_id"),
        indexes = {
            @Index(name = "idx_mapping_category", columnList = "category_id"),
            @Index(name = "idx_mapping_rule", columnList = "rule_id")
        }
    )
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
    private Set<BusinessRule> rules = new HashSet<>();
    
    // Audit fields
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Helper methods
    public void addRule(BusinessRule rule) {
        this.rules.add(rule);
//        rule.getCategories().add(this);
    }
    
    public void removeRule(BusinessRule rule) {
        this.rules.remove(rule);
        rule.getCategories().remove(this);
    }
    
    public String getFullPath() {
        List<String> path = new ArrayList<>();
        RuleCategory current = this;
        while (current != null) {
            path.add(0, current.getCategoryCode());
            current = current.getParentCategory();
        }
        return String.join("/", path);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotBlank(message = "Category code is required") String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(@NotBlank(message = "Category code is required") String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public @NotBlank(message = "Category name is required") String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(@NotBlank(message = "Category name is required") String categoryName) {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public RuleCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(RuleCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Set<RuleCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(Set<RuleCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public Set<BusinessRule> getRules() {
        return rules;
    }

    public void setRules(Set<BusinessRule> rules) {
        this.rules = rules;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RuleCategory() {
    }

    public RuleCategory(UUID id, String categoryCode, String categoryName, String description, String icon, String color, Integer sortOrder, boolean active, RuleCategory parentCategory, Set<RuleCategory> subCategories, Set<BusinessRule> rules,LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.description = description;
        this.icon = icon;
        this.color = color;
        this.sortOrder = sortOrder;
        this.active = active;
        this.parentCategory = parentCategory;
        this.subCategories = subCategories;
        this.rules = rules;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}