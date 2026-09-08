package com.mugide.cafe_popp_backend.repository;

import com.mugide.cafe_popp_backend.entity.DiningTable;
import com.mugide.cafe_popp_backend.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
	long countByStatus(TableStatus status);
}