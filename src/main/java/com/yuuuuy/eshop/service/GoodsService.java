package com.yuuuuy.eshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuuuuy.eshop.model.dto.GoodsDTO;
import com.yuuuuy.eshop.model.dto.GoodsToOrderDTO;
import com.yuuuuy.eshop.model.entity.Goods;
import com.yuuuuy.eshop.model.vo.GoodsDetailVO;
import com.yuuuuy.eshop.model.vo.GoodsVO;
import com.yuuuuy.eshop.model.dto.UpdateGoodsDTO;
import com.yuuuuy.eshop.model.vo.admin.AdminGoodsVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface GoodsService extends IService<Goods> {
    //通用
    public List<GoodsVO> selectPage(Integer page, Integer size);
    public List<GoodsVO> selectPageByCategoryId(Integer page, Integer size, Integer categoryId);
    public GoodsDetailVO selectGoodsById(Integer id);

    //admin
    public Boolean addGoods(MultipartFile multipartFile, GoodsDTO goodsDTO) throws IOException;
    public Boolean updateGoods(MultipartFile multipartFile, UpdateGoodsDTO data);
    public List<AdminGoodsVO> getGoodsinfo();
    public Boolean switchStatus(Integer id, Character status);
    public AdminGoodsVO getByGoodsId(Integer id);
    //1
    public HashMap<Integer, GoodsToOrderDTO> getGoodsInfoByids(List<Integer> ids);
    //1
    public BigDecimal getGoodsPrice(Integer goodsId);
    //1
    public Integer getSKU(Integer goodsId);
    //1
    public Boolean subSKU(Integer goodsId, Integer amount);

    public GoodsToOrderDTO getGoodsInfo(Integer id);
}
