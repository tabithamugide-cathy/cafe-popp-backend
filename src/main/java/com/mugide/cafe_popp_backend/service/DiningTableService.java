package com.mugide.cafe_popp_backend.service;

import com.mugide.cafe_popp_backend.entity.DiningTable;
import com.mugide.cafe_popp_backend.enums.TableStatus;
import com.mugide.cafe_popp_backend.repository.DiningTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiningTableService {

    private final DiningTableRepository diningTableRepository;

    public DiningTableService(DiningTableRepository diningTableRepository) {
        this.diningTableRepository = diningTableRepository;
    }

    public List<DiningTable> getAll() {
        return diningTableRepository.findAll();
    }

    public DiningTable getById(Long id) {
        return diningTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found: " + id));
    }

    public DiningTable create(DiningTable table) {
        return diningTableRepository.save(table);
    }
    public DiningTable updateStatus(Long id, TableStatus status) {
        DiningTable table = getById(id);
        table.setStatus(status);
        return diningTableRepository.save(table);
    }
}