package com.yuuuuy.eshop.controller.admin;

import com.yuuuuy.eshop.model.dto.GoodsDTO;
import com.yuuuuy.eshop.model.entity.Goods;
import com.yuuuuy.eshop.model.dto.UpdateGoodsDTO;
import com.yuuuuy.eshop.model.vo.admin.AdminGoodsVO;
import com.yuuuuy.eshop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("hasAuthority('manage_goods')")
@RequestMapping("/admin/goodsmanager")
public class ManageGoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 添加商品
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @PostMapping(value = "add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Boolean addGoods(@RequestPart(value = "img")MultipartFile multipartFile
                            ,@RequestParam String name
                            ,@RequestParam BigDecimal price
                            ,@RequestParam Integer sku
                            ,@RequestParam String description
                            ,@RequestParam Integer categoryId)throws IOException {
        GoodsDTO data = new GoodsDTO();
        data.setName(name);
        data.setPrice(price);
        data.setSKU(sku);
        data.setDescription(description);
        data.setCategoryId(categoryId);
        return goodsService.addGoods(multipartFile, data);
    }

    /**
     * 删除商品
     * @param id
     * @return
     */
    @DeleteMapping("del/{id}")
    public Boolean delGoods(Integer id){
        return goodsService.removeById(id);
    }

    @GetMapping("get")
    public List<AdminGoodsVO> getGoodsInfo(){
        return goodsService.getGoodsinfo();
    }

    @PostMapping ("/switch")
    public Boolean switchStatus(Integer id, Character status){
        return goodsService.switchStatus(id, status);
    }
    /**
     * 获取详细信息, 显示到修改页面, 让管理员进行修改
     * @param id
     * @return
     */
    @GetMapping("check")
    public AdminGoodsVO checkGoods(Integer id){
        return goodsService.getByGoodsId(id);
    }

    /**
     * 修改商品信息
     * @param multipartFile
     * @return
     */
    @PostMapping("update")
    public Boolean updateGoods(@RequestPart(value = "img", required = false)MultipartFile multipartFile
            ,@RequestParam Integer id
            ,@RequestParam String name
            ,@RequestParam BigDecimal price
            ,@RequestParam Integer sku
            ,@RequestParam String description
            ,@RequestParam Integer categoryId){
        UpdateGoodsDTO data = new UpdateGoodsDTO();
        data.setId(id);
        data.setName(name);
        data.setPrice(price);
        data.setSKU(sku);
        data.setDescription(description);
        data.setCategoryId(categoryId);
        return goodsService.updateGoods(multipartFile, data);
    }
}
