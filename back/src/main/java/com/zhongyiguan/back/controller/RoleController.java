package com.zhongyiguan.back.controller;

import com.zhongyiguan.back.entity.Role;
import com.zhongyiguan.back.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com/back/api/role")
public class RoleController {

    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/list")
    public List<Role> list() {
        return roleService.list();
    }

    @GetMapping("/{id}")
    public Role get(@PathVariable("id") String id) {
        return roleService.get(id);
    }

    @PostMapping
    public boolean create(@RequestBody Role body) {
        return roleService.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody Role body) {
        return roleService.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id) {
        return roleService.delete(id);
    }
}
