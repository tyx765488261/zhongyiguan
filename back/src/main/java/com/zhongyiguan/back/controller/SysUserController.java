package com.zhongyiguan.back.controller;

import com.zhongyiguan.back.entity.SysUser;
import com.zhongyiguan.back.service.ISysUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com/back/api/user")
public class SysUserController {

    private final ISysUserService sysUserService;

    public SysUserController(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping("/list")
    public List<SysUser> list() {
        return sysUserService.list();
    }

    @GetMapping("/{id}")
    public SysUser get(@PathVariable("id") String id) {
        return sysUserService.get(id);
    }

    @PostMapping
    public boolean create(@RequestBody SysUser body) {
        return sysUserService.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody SysUser body) {
        return sysUserService.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id) {
        return sysUserService.delete(id);
    }
}
