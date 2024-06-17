package com.yuuuuy.eshop.controller.universal;

import com.yuuuuy.eshop.model.dto.RegisterDTO;
import com.yuuuuy.eshop.model.params.LoginParam;
import com.yuuuuy.eshop.model.vo.CategoryVO;
import com.yuuuuy.eshop.model.vo.GoodsDetailVO;
import com.yuuuuy.eshop.model.vo.GoodsVO;
import com.yuuuuy.eshop.model.vo.TokenVO;
import com.yuuuuy.eshop.service.CategoryService;
import com.yuuuuy.eshop.service.GoodsService;
import com.yuuuuy.eshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/universal")
public class UniversalController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    /**
     * 返回商品列表, 用于首页商品展示
     * @param page
     * @param size
     * @return
     */
    @GetMapping("page")
    public List<GoodsVO> selectPage(Integer page, Integer size){
        return goodsService.selectPage(page, size);
    }

    /**
     * 返回分类信息
     * @return
     */
    @GetMapping("categories")
    public List<CategoryVO> getCategories(){
        return categoryService.getCategories();
    }

    /**
     * 根据分类返回商品信息
     * @param page
     * @param size
     * @param categoryId
     * @return
     */
    @GetMapping("pagebycategory")
    public List<GoodsVO> selectPageByCategoryId(Integer page, Integer size, Integer categoryId){
        return goodsService.selectPageByCategoryId(page, size, categoryId);
    }
    @GetMapping("detail")
    public GoodsDetailVO selectGoodsById(Integer id){
        return goodsService.selectGoodsById(id);
    }

    @PostMapping("login")
    public TokenVO login(@RequestBody LoginParam loginParam){
        return userService.login(loginParam);
    }

    @PostMapping("register")
    public Boolean register(@RequestBody RegisterDTO registerDTO){
        return userService.register(registerDTO);
    }
}
