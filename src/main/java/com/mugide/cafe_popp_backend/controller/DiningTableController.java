package com.mugide.cafe_popp_backend.controller;

import com.mugide.cafe_popp_backend.entity.DiningTable;
import com.mugide.cafe_popp_backend.enums.TableStatus;
import com.mugide.cafe_popp_backend.service.DiningTableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class DiningTableController {

    private final DiningTableService diningTableService;

    public DiningTableController(DiningTableService diningTableService) {
        this.diningTableService = diningTableService;
    }

    @GetMapping
    public List<DiningTable> getAll() {
        return diningTableService.getAll();
    }

    @GetMapping("/{id}")
    public DiningTable getById(@PathVariable Long id) {
        return diningTableService.getById(id);
    }

    @PostMapping
    public DiningTable create(@RequestBody DiningTable table) {
        return diningTableService.create(table);
    }

    @PatchMapping("/{id}/status")
    public DiningTable updateStatus(@PathVariable Long id, @RequestParam TableStatus status) {
        return diningTableService.updateStatus(id, status);
    }
}