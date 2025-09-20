package com.example.clientZeebe.common.drools.entity;

import com.example.clientZeebe.common.drools.enums.RuleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "business_rules")
@EntityListeners(AuditingEntityListener.class)
public class BusinessRule {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "rule_name", nullable = false, length = 255)
    @NotBlank(message = "Rule name is required")
    private String ruleName;
    
    @Column(name = "rule_content", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Rule content is required")
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String ruleContent;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "rule_type", length = 50)
    @Enumerated(EnumType.STRING)
    private RuleType ruleType = RuleType.DRL;
    
    @Column(name = "agenda_group", length = 100)
    private String agendaGroup;
    
    @Column(name = "ruleflow_group", length = 100)
    private String ruleflowGroup;
    
    @Column(name = "activation_group", length = 100)
    private String activationGroup;
    
    @Column(name = "priority")
    private Integer priority = 0;
    
    @Column(name = "version")
    @Version
    private Integer version = 1;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    // Tags for quick filtering
    @ElementCollection
    @CollectionTable(name = "rule_tags", joinColumns = @JoinColumn(name = "rule_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();
    
    // Many-to-Many relationship with categories
//    @ManyToMany(mappedBy = "rules", fetch = FetchType.LAZY)
    @ElementCollection
    private Set<String> categories = new HashSet<>();
    
    // Audit fields
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
    
    // Helper methods
    public void addCategory(RuleCategory category) {
//        this.categories.add(category);
//        category.getRules().add(this);
    }
    
    public void removeCategory(RuleCategory category) {
        this.categories.remove(category);
        category.getRules().remove(this);
    }
    
//    public Set<String> getCategoryKeys() {
//        Set<String> keys = new HashSet<>();
//        for (RuleCategory category : categories) {
//            keys.add(category.getCategoryCode());
//        }
//        return keys;
//    }

    public BusinessRule() {
    }

    public BusinessRule(UUID id, String ruleName, String ruleContent, String description, RuleType ruleType, String agendaGroup, String ruleflowGroup, String activationGroup, Integer priority, Integer version, boolean active, Set<String> tags, Set<String> categories, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.ruleName = ruleName;
        this.ruleContent = ruleContent;
        this.description = description;
        this.ruleType = ruleType;
        this.agendaGroup = agendaGroup;
        this.ruleflowGroup = ruleflowGroup;
        this.activationGroup = activationGroup;
        this.priority = priority;
        this.version = version;
        this.active = active;
        this.tags = tags;
        this.categories = categories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotBlank(message = "Rule name is required") String getRuleName() {
        return ruleName;
    }

    public void setRuleName(@NotBlank(message = "Rule name is required") String ruleName) {
        this.ruleName = ruleName;
    }

    public @NotBlank(message = "Rule content is required") String getRuleContent() {
        return ruleContent;
    }

    public void setRuleContent(@NotBlank(message = "Rule content is required") String ruleContent) {
        this.ruleContent = ruleContent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public String getAgendaGroup() {
        return agendaGroup;
    }

    public void setAgendaGroup(String agendaGroup) {
        this.agendaGroup = agendaGroup;
    }

    public String getRuleflowGroup() {
        return ruleflowGroup;
    }

    public void setRuleflowGroup(String ruleflowGroup) {
        this.ruleflowGroup = ruleflowGroup;
    }

    public String getActivationGroup() {
        return activationGroup;
    }

    public void setActivationGroup(String activationGroup) {
        this.activationGroup = activationGroup;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}