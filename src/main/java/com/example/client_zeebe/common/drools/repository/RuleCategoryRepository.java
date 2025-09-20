package com.example.clientZeebe.common.drools.repository;

import com.example.clientZeebe.common.drools.entity.RuleCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface RuleCategoryRepository extends JpaRepository<RuleCategory, UUID>,
                                               JpaSpecificationExecutor<RuleCategory> {
    
    Optional<RuleCategory> findByCategoryCode(String categoryCode);
    
    List<RuleCategory> findByActiveTrue();
    
    List<RuleCategory> findByParentCategoryIsNullAndActiveTrue();
    
    List<RuleCategory> findByParentCategory_Id(UUID parentId);
    
    @Query("SELECT c FROM RuleCategory c WHERE c.parentCategory.categoryCode = :parentCode")
    List<RuleCategory> findByParentCategoryCode(@Param("parentCode") String parentCode);
    
    @Query("SELECT c FROM RuleCategory c LEFT JOIN FETCH c.rules WHERE c.id = :id")
    Optional<RuleCategory> findByIdWithRules(@Param("id") UUID id);
    
    @Query("SELECT c FROM RuleCategory c LEFT JOIN FETCH c.subCategories WHERE c.id = :id")
    Optional<RuleCategory> findByIdWithSubCategories(@Param("id") UUID id);
    
    @Query("SELECT COUNT(c) > 0 FROM RuleCategory c WHERE c.categoryCode = :code")
    boolean existsByCategoryCode(@Param("code") String code);
    
    @Query(value = "WITH RECURSIVE category_tree AS (" +
           "  SELECT * FROM rule_categories WHERE id = :categoryId " +
           "  UNION ALL " +
           "  SELECT rc.* FROM rule_categories rc " +
           "  INNER JOIN category_tree ct ON rc.parent_category_id = ct.id" +
           ") SELECT * FROM category_tree",
            nativeQuery = true)
    List<RuleCategory> findCategoryTree(@Param("categoryId") UUID categoryId);
    
    @Modifying
    @Query("UPDATE RuleCategory c SET c.sortOrder = :sortOrder WHERE c.id = :id")
    void updateSortOrder(@Param("id") UUID id, @Param("sortOrder") Integer sortOrder);
    
    Page<RuleCategory> findByCategoryNameContainingIgnoreCase(String name, Pageable pageable);
}