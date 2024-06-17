package com.yuuuuy.eshop;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.yuuuuy.eshop.dao.CategoryMapper;
import com.yuuuuy.eshop.dao.GoodsMapper;
import com.yuuuuy.eshop.dao.UserMapper;
import com.yuuuuy.eshop.model.entity.Category;
import com.yuuuuy.eshop.model.entity.Goods;
import com.yuuuuy.eshop.model.entity.User;
import com.yuuuuy.eshop.model.vo.CategoryVO;
import com.yuuuuy.eshop.model.vo.GoodsVO;
import com.yuuuuy.eshop.model.dto.UpdateGoodsDTO;
import com.yuuuuy.eshop.service.*;
import com.yuuuuy.eshop.utils.JWTUtils;
import com.yuuuuy.eshop.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
@Slf4j
@SpringBootTest
class EshopApplicationTests {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisUtils redisUtils;
    @Resource
    RoleAuthorityService roleAuthorityService;
    @Resource
    AuthorityService authorityService;
    @Resource
    PasswordEncoder passwordEncoder;
    @Test
    void contextLoads() {
        Goods goods = new Goods();
        goods.setId(2223);
        goods.setDescription("ddd");
        goods.setName("zi");
        goods.setPrice(new BigDecimal("23.22"));
        goods.setSKU(22);
        goods.setImgPath("eeeeeeeeee");
        goods.setCategoryId(1);
        new org.springframework.security.core.userdetails.User(null, null, null);
        goodsMapper.insert(goods);
    }


    @Test
    void select2(){
        List<CategoryVO> list = categoryService.getCategories();
        System.out.println(list);
    }

    @Test
    void select3(){
        List<GoodsVO> goodsVO = goodsService.selectPage(1, 3);
        System.out.println(goodsVO);
        List<GoodsVO> goodsVOS = goodsService.selectPageByCategoryId(1, 3, 1);
        System.out.println(goodsVOS);
        log.info(goodsVOS.toString());
        //categoryService.addCategory("j2j");
//        Category category = new Category();
//        category.setId(44);
//        category.setName("43");
//        categoryMapper.insert(category);
        categoryService.removeById(4);
    }

    private String key = "yuuuuy";
    @Test
    void createJWT(){
        HashMap<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("id", "1");
        payload.put("name", "yuuuuy");
        String token = JWTUtil.createToken(header, payload, key.getBytes());
        log.info(token);
    }

    @Test
    void parseToken(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoieXV1dXV5IiwiaWQiOiIxIn0.WiJUa2mPeLFBWwCEeg3FOQCQCpr_OMEoHZ6FghAkGAw";
        try {
            JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
            log.info(jwt.getPayloads().toString());
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Test
    void getPath(){
        log.info(System.getProperty("user.dir"));
    }

    @Test
    void upd(){
        UpdateGoodsDTO updateGoodsDTO = new UpdateGoodsDTO();
        updateGoodsDTO.setId(12);
        updateGoodsDTO.setName("wo");
        updateGoodsDTO.setDescription("ss");
        updateGoodsDTO.setCategoryId(1);
        goodsService.updateGoods(null, updateGoodsDTO);
    }

    @Test
    void tokenTest(){
        String token = JWTUtils.createToken(1);
        log.info(token);
        Boolean validate = JWTUtils.validate(token);
        log.info(validate + "");
    }

    @Test
    void passwordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("11223");
        log.info("加密密文：{}", password);
        boolean matches = passwordEncoder.matches("11223", password);
        log.info("是否匹配：{}", matches);
    }

    @Test
    void usertest(){
        User user = userMapper.selectById(4);
        String password = user.getPassword();
        log.info(password);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pw = passwordEncoder.encode("saogang");
        log.info("加密密文：{}", pw);
        boolean matches = passwordEncoder.matches("saogang", password);
        log.info("是否匹配：{}", matches);
    }

    @Test
    void test(){
        String str = "10order:7";
        // 找到 "order:" 后面的位置
        int index = str.indexOf("order:") + "order:".length();
        // 使用 substring 提取数字部分
        String number = str.substring(index);
        // 输出提取的数字
        System.out.println(number);
    }

    @Test
    void tes2t(){
        List<Integer> authorityIds = roleAuthorityService.getAuthorityIds(1);
        log.info(authorityIds.toString());
        List<String> authorities = authorityService.getAuthorities(authorityIds);
        log.info(authorities.toString());
    }
    @Test
    void tes3t() throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        System.out.println(System.getProperty("user.dir"));
    }
    @Test
    void tes4t(){
        Category category = new Category();
        category.setName("www");
        categoryMapper.insert(category);

    }
}
