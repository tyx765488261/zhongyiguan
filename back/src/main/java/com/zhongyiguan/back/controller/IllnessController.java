package com.zhongyiguan.back.controller;

import com.zhongyiguan.back.entity.Illness;
import com.zhongyiguan.back.service.IIllnessService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com/back/api/illness")
public class IllnessController {

    private final IIllnessService illnessService;

    public IllnessController(IIllnessService illnessService) {
        this.illnessService = illnessService;
    }

    @GetMapping("/list")
    public List<Illness> list(@RequestParam(value = "departmentId", required = false) String departmentId) {
        return illnessService.list(departmentId);
    }

    @GetMapping("/{id}")
    public Illness get(@PathVariable("id") String id) {
        return illnessService.get(id);
    }

    @PostMapping
    public boolean create(@RequestBody Illness body) {
        return illnessService.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody Illness body) {
        return illnessService.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id) {
        return illnessService.delete(id);
    }
}

