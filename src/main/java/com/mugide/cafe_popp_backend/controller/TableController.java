package com.mugide.cafe_popp_backend.controller;

import com.mugide.cafe_popp_backend.entity.DiningTable;
import com.mugide.cafe_popp_backend.enums.TableStatus;
import com.mugide.cafe_popp_backend.repository.DiningTableRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final DiningTableRepository diningTableRepository;

    public TableController(DiningTableRepository diningTableRepository) {
        this.diningTableRepository = diningTableRepository;
    }

    @GetMapping
    public List<DiningTable> getTables() {
        return diningTableRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER', 'WAITER')")
    @ResponseStatus(HttpStatus.CREATED)
    public DiningTable createTable(@RequestBody TableRequest request) {
        if (request.tableNumber() == null || request.tableNumber() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Table number must be positive");
        }
        if (request.capacity() == null || request.capacity() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Capacity must be positive");
        }

        DiningTable table = new DiningTable();
        table.setTableNumber(request.tableNumber());
        table.setCapacity(request.capacity());
        table.setStatus(TableStatus.FREE);
        return diningTableRepository.save(table);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER', 'WAITER')")
    public DiningTable updateStatus(@PathVariable Long id, @RequestParam TableStatus status) {
        DiningTable table = diningTableRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found"));
        table.setStatus(status);
        return diningTableRepository.save(table);
    }

    public record TableRequest(Integer tableNumber, Integer capacity) {
    }
}
