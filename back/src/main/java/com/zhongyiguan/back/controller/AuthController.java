package com.zhongyiguan.back.controller;

import com.zhongyiguan.back.entity.Auth;
import com.zhongyiguan.back.service.IAuthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com/back/api/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/list")
    public List<Auth> list() {
        return authService.list();
    }

    @GetMapping("/{id}")
    public Auth get(@PathVariable("id") String id) {
        return authService.get(id);
    }

    @PostMapping
    public boolean create(@RequestBody Auth body) {
        return authService.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody Auth body) {
        return authService.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id) {
        return authService.delete(id);
    }
}
