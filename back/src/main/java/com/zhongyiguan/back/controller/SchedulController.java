package com.zhongyiguan.back.controller;

import com.zhongyiguan.back.entity.Schedul;
import com.zhongyiguan.back.service.ISchedulService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com/back/api/schedul")
public class SchedulController {

    private final ISchedulService schedulService;

    public SchedulController(ISchedulService schedulService) {
        this.schedulService = schedulService;
    }

    @GetMapping("/list")
    public List<Schedul> list(@RequestParam(value = "doctorId", required = false) String doctorId) {
        return schedulService.list(doctorId);
    }

    @GetMapping("/{id}")
    public Schedul get(@PathVariable("id") String id) {
        return schedulService.get(id);
    }

    @PostMapping
    public boolean create(@RequestBody Schedul body) {
        return schedulService.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody Schedul body) {
        return schedulService.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id) {
        return schedulService.delete(id);
    }
}
