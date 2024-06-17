package com.yuuuuy.eshop.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuuuuy.eshop.dao.GoodsMapper;
import com.yuuuuy.eshop.model.dto.GoodsDTO;
import com.yuuuuy.eshop.model.dto.GoodsToOrderDTO;
import com.yuuuuy.eshop.model.dto.UdPriceDTO;
import com.yuuuuy.eshop.model.entity.Goods;
import com.yuuuuy.eshop.model.vo.GoodsDetailVO;
import com.yuuuuy.eshop.model.vo.GoodsVO;
import com.yuuuuy.eshop.model.dto.UpdateGoodsDTO;
import com.yuuuuy.eshop.model.vo.admin.AdminGoodsVO;
import com.yuuuuy.eshop.service.CartGoodsService;
import com.yuuuuy.eshop.service.CartService;
import com.yuuuuy.eshop.service.CategoryService;
import com.yuuuuy.eshop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Autowired
    CartGoodsService cartGoodsService;
    @Autowired
    CartService cartService;
    @Autowired
    CategoryService categoryService;
    private static final String IMGPATH = System.getProperty("user.dir") + "\\static\\img\\";
    private static final Integer MAX_SIZE = 20;
    @Override
    public List<GoodsVO> selectPage(Integer page, Integer size) {
        //单页请求数过多
        if(size > 20){
            throw new RuntimeException("单页请求过多商品信息");
        }
        Page<Goods> gPage = new Page<>(page, size);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getDelFlag, "0")
                .eq(Goods::getStatus, "0")
                .select(Goods::getId,
                        Goods::getName,
                        Goods::getPrice,
                        Goods::getImgPath);
        List<GoodsVO> collect = baseMapper.selectPage(gPage, wrapper).getRecords()
                .stream()
                .map(entity -> {
                    GoodsVO goodsVO = new GoodsVO();
                    goodsVO.fromEntity(entity);
                    return goodsVO;
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<GoodsVO> selectPageByCategoryId(Integer page, Integer size, Integer categoryId) {
        Page<Goods> gPage = new Page<>(page, size);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getDelFlag, 0)
                .eq(Goods::getCategoryId, categoryId)
                .select(Goods::getId,
                        Goods::getName,
                        Goods::getPrice,
                        Goods::getImgPath);
        List<GoodsVO> collect = baseMapper.selectPage(gPage, wrapper).getRecords()
                .stream()
                .map(entity -> {
                    GoodsVO goodsVO = new GoodsVO();
                    goodsVO.fromEntity(entity);
                    return goodsVO;
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public GoodsDetailVO selectGoodsById(Integer id) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getId, id)
                .select(Goods::getId,
                        Goods::getName,
                        Goods::getDescription,
                        Goods::getPrice,
                        Goods::getImgPath,
                        Goods::getSales);
        Goods goods = baseMapper.selectOne(wrapper);
        if(goods != null){
            GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
            goodsDetailVO.fromEntity(goods);
            return goodsDetailVO;
        }
        return null;
    }

    @Override
    public AdminGoodsVO getByGoodsId(Integer id) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getDelFlag, "0")
                .eq(Goods::getId, id)
                .select(Goods::getName
                        ,Goods::getDescription
                        ,Goods::getPrice
                        ,Goods::getSKU
                        ,Goods::getCategoryId
                        ,Goods::getSales
                        ,Goods::getStatus);
        Goods goods = baseMapper.selectOne(wrapper);
        AdminGoodsVO adminGoodsVO = new AdminGoodsVO();
        adminGoodsVO.transfer(goods);
        String name = categoryService.getName(goods.getCategoryId());
        adminGoodsVO.setCategory(name);
        return adminGoodsVO;
    }

    @Override
    public Boolean addGoods(MultipartFile multipartFile, GoodsDTO goodsDTO){
        //TODO 判断文件类型、大小
        //图片保存到static/img目录下,在文件名前加一个uuid,防止重名
        String filename = multipartFile.getOriginalFilename();
        String uuid = IdUtil.fastSimpleUUID();
        String targetPath = IMGPATH + uuid + filename;
        //TODO 处理异常
        try {
            FileUtil.writeBytes(multipartFile.getBytes(), targetPath);
        }catch (IOException e){
        }
        //对象持久化
        Goods goods = new Goods();
        goods.fromDTO(goodsDTO);
        String filePath = uuid + filename;
        goods.setImgPath(filePath);
        int insert = baseMapper.insert(goods);
        return true;
    }

    @Override
    public Boolean updateGoods(MultipartFile multipartFile, UpdateGoodsDTO data) {
        Goods goods = new Goods();
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goods::getId, data.getId())
                .select(Goods::getImgPath, Goods::getPrice);
        Goods selectedOne = baseMapper.selectOne(queryWrapper);
        //判断前端是否传输了图片, 有就更新(删除原来的, 修改路径)
        if(multipartFile != null){
            //TODO 判断文件类型和大小
            //旧图片
            String oldPath = IMGPATH + selectedOne.getImgPath();
            FileUtil.del(new File(oldPath));
            //新图片
            String newPath = IdUtil.fastSimpleUUID() + multipartFile.getOriginalFilename();
            String targetPath = IMGPATH + newPath;
            //TODO 处理异常
            try {
                FileUtil.writeBytes(multipartFile.getBytes(), targetPath);
            }catch (IOException e){

            }
            goods.setImgPath(newPath);
        }
        //判断价格是否改变
        if(selectedOne.getPrice() != data.getPrice()){
            //获取购物车id, 商品id
            //TODO 应该分两个功能的, fk
            //获取原来的价格、数量, 更新购物车里商品总价
            List<UdPriceDTO> udPriceDTOS = cartGoodsService.updatePrice(data.getId(), data.getPrice());
            //计算价格差, key是cartId, value是差价
            HashMap<Integer, BigDecimal> map = new HashMap<>();
            for(UdPriceDTO udPriceDTO : udPriceDTOS){
                Integer amount = udPriceDTO.getAmount();
                BigDecimal prePrice = udPriceDTO.getPrePrice();
                BigDecimal curPrice = data.getPrice();
                BigDecimal priceGap = curPrice.subtract(prePrice).multiply(new BigDecimal(amount));
                map.put(udPriceDTO.getCartId(), priceGap);
            }
            //TODO 异步
            //更新购物车总价格(价格差)
            for(Integer key : map.keySet()){
                cartService.updatePrice(key, map.get(key));
            }
        }
        goods.fromUGDTO(data);
        baseMapper.updateById(goods);
        return true;
    }

    @Override
    public List<AdminGoodsVO> getGoodsinfo() {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getDelFlag, "0")
                .select(Goods::getId
                        ,Goods::getName
                        ,Goods::getDescription
                        ,Goods::getPrice
                        ,Goods::getImgPath
                        ,Goods::getSKU
                        ,Goods::getCategoryId
                        ,Goods::getSales
                        ,Goods::getStatus);
        List<Goods> goods = baseMapper.selectList(wrapper);
        List<AdminGoodsVO> collect = goods.stream()
                .map(entity -> {
                    AdminGoodsVO adminGoodsVO = new AdminGoodsVO();
                    adminGoodsVO.transfer(entity);
                    String name = categoryService.getName(entity.getCategoryId());
                    adminGoodsVO.setCategory(name);
                    return adminGoodsVO;
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public Boolean switchStatus(Integer id, Character status) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setStatus(status);
        int i = baseMapper.updateById(goods);
        return i==1;
    }

    @Override
    public HashMap<Integer, GoodsToOrderDTO> getGoodsInfoByids(List<Integer> ids) {
        HashMap<Integer, GoodsToOrderDTO> map = new HashMap<>();
        for (Integer id : ids){
            LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(Goods::getName, Goods::getImgPath);
            wrapper.eq(Goods::getId, id);
            Goods goods = baseMapper.selectOne(wrapper);
            GoodsToOrderDTO goodsToOrderDTO = new GoodsToOrderDTO();
            goodsToOrderDTO.setName(goods.getName());
            goodsToOrderDTO.setImgPath(goods.getImgPath());
            map.put(id, goodsToOrderDTO);
        }
        return map;
    }

    @Override
    public BigDecimal getGoodsPrice(Integer goodsId) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Goods::getPrice)
                .eq(Goods::getId, goodsId);
        Goods goods = baseMapper.selectOne(wrapper);
        BigDecimal price = goods.getPrice();
        return price;
    }

    @Override
    public Integer getSKU(Integer goodsId) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getId, goodsId)
                .select(Goods::getSKU);
        Goods goods = baseMapper.selectOne(wrapper);
        Integer sku = goods.getSKU();
        return sku;
    }

    @Override
    public Boolean subSKU(Integer goodsId, Integer amount) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getId, goodsId)
                .select(Goods::getSKU);
        Goods goods = baseMapper.selectOne(wrapper);
        Integer sku = goods.getSKU();
        sku = sku - amount;
        goods.setSKU(sku);
        goods.setId(goodsId);
        baseMapper.updateById(goods);
        return true;
    }

    @Override
    public GoodsToOrderDTO getGoodsInfo(Integer id) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getId, id)
                .select(Goods::getName, Goods::getImgPath);
        Goods goods = baseMapper.selectOne(wrapper);
        GoodsToOrderDTO goodsToOrderDTO = new GoodsToOrderDTO();
        goodsToOrderDTO.setName(goods.getName());
        goodsToOrderDTO.setImgPath(goods.getImgPath());
        return goodsToOrderDTO;
    }
}
