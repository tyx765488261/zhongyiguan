package com.zhongyiguan.back.controller;

import com.zhongyiguan.back.entity.Evaluate;
import com.zhongyiguan.back.service.IEvaluateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com/back/api/evaluate")
public class EvaluateController {

    private final IEvaluateService evaluateService;

    public EvaluateController(IEvaluateService evaluateService) {
        this.evaluateService = evaluateService;
    }

    @GetMapping("/list")
    public List<Evaluate> list(@RequestParam(value = "doctorId", required = false) String doctorId) {
        return evaluateService.list(doctorId);
    }

    @GetMapping("/{id}")
    public Evaluate get(@PathVariable("id") String id) {
        return evaluateService.get(id);
    }

    @PostMapping
    public boolean create(@RequestBody Evaluate body) {
        return evaluateService.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody Evaluate body) {
        return evaluateService.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id) {
        return evaluateService.delete(id);
    }
}
