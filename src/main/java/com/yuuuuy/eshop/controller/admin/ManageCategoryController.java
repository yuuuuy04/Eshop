package com.yuuuuy.eshop.controller.admin;

import com.yuuuuy.eshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAuthority('manage_category')")
@RequestMapping("/admin/cate")
public class ManageCategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 添加分类
     * @param name
     * @return
     */
    @PostMapping("add")
    public Boolean addCategory(String name){
        return categoryService.addCategory(name);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("del")
    public Boolean delCategory(Integer id){
        return categoryService.removeById(id);
    }
}
