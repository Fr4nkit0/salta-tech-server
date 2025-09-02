package com.saltaTech.product.domain.repository;

import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import com.saltaTech.product.domain.persistence.BranchStock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchStockRepository extends SoftDeleteRepository<BranchStock,Long> {
	@Query("select bs from BranchStock bs where bs.enabled = true and bs.product.id = ?1")
	List<BranchStock> findAllByBranchIdAndProductIdIn(Long branchId, List<Long> productIds);
	@Query("select bs from BranchStock bs where bs.enabled = true and bs.branch.id = ?1 and bs.product.id = ?2")
	Optional<BranchStock> findByBranchIdAndProductId(Long branchId, Long productId);
	@Modifying
	@Query("update BranchStock bs set bs.enabled = false, bs.deletedDate = CURRENT_TIMESTAMP where bs.product.id = ?1")
	void deleteAllByProductId(Long productId);
}
