-- eshop.authority definition

CREATE TABLE `authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` char(64) NOT NULL COMMENT '权限',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='权限表';


-- eshop.cart definition

CREATE TABLE `cart` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `total_prices` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '购物车商品总价',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '购物车状态',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopping_cart_un` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='购物车';


-- eshop.cart_goods definition

CREATE TABLE `cart_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cart_id` bigint(20) NOT NULL COMMENT '所属购物车id',
  `goods_id` bigint(20) NOT NULL COMMENT '商品id',
  `amount` int(11) NOT NULL COMMENT '商品数量',
  `total_prices` decimal(10,0) NOT NULL COMMENT '该商品总价',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='购物车商品';


-- eshop.category definition

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '分类',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `category_un` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='分类';


-- eshop.e_order definition

CREATE TABLE `e_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number` bigint(20) NOT NULL COMMENT '寻找订单',
  `user_id` bigint(20) NOT NULL COMMENT '订单所属用户',
  `total_prices` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '商品总价',
  `status` char(1) NOT NULL DEFAULT '2' COMMENT '是否完成(0未完成, 1已完成, 2未支付, 3取消)',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_un` (`number`),
  KEY `e_order_status_IDX` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='订单';


-- eshop.goods definition

CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` text NOT NULL COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `img_path` varchar(128) NOT NULL DEFAULT '' COMMENT '商品图片路径',
  `SKU` int(11) NOT NULL DEFAULT '0' COMMENT '商品数量',
  `category_id` int(11) NOT NULL COMMENT '分类id',
  `sales` int(11) NOT NULL DEFAULT '0' COMMENT '商品销量',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '商品是否启用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='商品表';


-- eshop.order_goods definition

CREATE TABLE `order_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL COMMENT '所属订单id',
  `goods_id` bigint(20) NOT NULL COMMENT '商品id',
  `amount` int(11) NOT NULL COMMENT '商品数量',
  `total_prices` decimal(10,0) NOT NULL COMMENT '该商品总价',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='订单商品';


-- eshop.`role` definition

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '角色名',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色';


-- eshop.role_authority definition

CREATE TABLE `role_authority` (
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `authority_id` int(11) NOT NULL COMMENT '权限id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关系表';


-- eshop.`user` definition

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) NOT NULL COMMENT '用户名',
  `nick_name` varchar(64) DEFAULT NULL COMMENT '昵称',
  `phone_number` varchar(32) DEFAULT NULL COMMENT '电话',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `password` varchar(256) NOT NULL COMMENT '密码',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '用户余额',
  `sex` char(1) DEFAULT NULL COMMENT '性别',
  `avatar` varchar(128) DEFAULT NULL COMMENT '头像图片路径',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '账号是否启用',
  `user_type` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户角色(0普通用户, 1管理员)',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '信息是否删除(0未删除, 1删除)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_un` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='用户表';