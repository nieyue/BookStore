# 数据库 
#创建数据库
DROP DATABASE IF EXISTS book_store_db;
CREATE DATABASE book_store_db;

#使用数据库 
use book_store_db;

#创建书类型表 
CREATE TABLE book_cate_tb(
book_cate_id int(11) NOT NULL AUTO_INCREMENT COMMENT '书类型id',
name varchar(255) COMMENT '书类型名称',
update_date datetime COMMENT '更新时间',
PRIMARY KEY (book_cate_id)
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='书类型表';

#创建书表 
CREATE TABLE book_tb(
book_id int(11) NOT NULL AUTO_INCREMENT COMMENT '书id',
title varchar(255) COMMENT '书名称',
summary longtext COMMENT '简介',
author varchar(255) COMMENT '作者',
img_address varchar(255) COMMENT '封面',
chapter_number int(11) DEFAULT 0 COMMENT '章节数',
word_number bigint(20) DEFAULT 0 COMMENT '字数',
recommend tinyint(4) DEFAULT 0 COMMENT '推荐，默认0不推，1封推，2精推，3优推',
cost tinyint(4) DEFAULT 0 COMMENT '费用，默认为0免费，1为收费',
collect_number bigint(20) DEFAULT 0 COMMENT '收藏数',
pvs bigint(20) DEFAULT 0 COMMENT 'pv',
uvs bigint(20) DEFAULT 0 COMMENT 'uv',
ips bigint(20) DEFAULT 0 COMMENT 'ip',
reading_number bigint(20) DEFAULT 0 COMMENT '阅读数',
status tinyint(4) DEFAULT 1 COMMENT '下架0,上架1',
book_cate_id int(11) COMMENT '书类型id,外键',
create_date datetime  COMMENT '书创建时间',
update_date datetime  COMMENT '书更新时间',
PRIMARY KEY (book_id),
INDEX INDEX_CHAPTERNUMBER (chapter_number) USING BTREE,
INDEX INDEX_WORDNUMBER (word_number) USING BTREE,
INDEX INDEX_COST (cost) USING BTREE,
INDEX INDEX_COLLECTNUMBER (collect_number) USING BTREE,
INDEX INDEX_READINGNUMBER (reading_number) USING BTREE,
INDEX INDEX_BOOKCATEID (book_cate_id) USING BTREE,
INDEX INDEX_CREATEDATE (create_date) USING BTREE,
INDEX INDEX_UPDATEDATE (update_date) USING BTREE,
INDEX INDEX_STATUS (status) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='书表';

#创建书收藏表 
CREATE TABLE book_collect_tb(
collect_id int(11) NOT NULL AUTO_INCREMENT COMMENT '书收藏id',
create_date datetime COMMENT '创建时间',
book_id int(11) COMMENT '书id外键',
acount_id int(11) COMMENT '收藏人id外键',
PRIMARY KEY (collect_id),
INDEX INDEX_BOOKID (book_id) USING BTREE,
INDEX INDEX_ACOUNTID (acount_id) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='书收藏表';

#创建书章节表 
CREATE TABLE book_chapter_tb(
book_chapter_id int(11) NOT NULL AUTO_INCREMENT COMMENT '书章节id',
number int(11) COMMENT '书章节章数',
word_number bigint(20) DEFAULT 0 COMMENT '字数',
title varchar(255) COMMENT '书章节名称',
content longtext COMMENT '内容',
cost tinyint(4) DEFAULT 0 COMMENT '费用，默认为0免费，1为收费',
status tinyint(4) DEFAULT 1 COMMENT '下架0,上架1',
book_id int(11) COMMENT '书id,外键',
create_date datetime  COMMENT '书创建时间',
update_date datetime  COMMENT '书更新时间',
PRIMARY KEY (book_chapter_id),
INDEX INDEX_COST (cost) USING BTREE,
INDEX INDEX_NUMBER (number) USING BTREE,
INDEX INDEX_WORDNUMBER (word_number) USING BTREE,
INDEX INDEX_BOOKID (book_id) USING BTREE,
INDEX INDEX_CREATEDATE (create_date) USING BTREE,
INDEX INDEX_UPDATEDATE (update_date) USING BTREE,
INDEX INDEX_STATUS (status) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='书章节表';

#创建书订单表 
CREATE TABLE book_order_tb(
book_order_id int(11) NOT NULL AUTO_INCREMENT COMMENT '书订单id',
order_number varchar(255) COMMENT '订单号',
create_date datetime  COMMENT '创建时间',
update_date datetime  COMMENT '更新时间',
acount_id int(11) COMMENT '下单人',
PRIMARY KEY (book_order_id),
INDEX INDEX_ACOUNTID (acount_id) USING BTREE,
INDEX INDEX_CREATEDATE (create_date) USING BTREE,
INDEX INDEX_UPDATEDATE (update_date) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='书订单表';

#创建书订单详情表 
CREATE TABLE book_order_detail_tb(
book_order_detail_id int(11) NOT NULL AUTO_INCREMENT COMMENT '书订单详情id',
billing_mode tinyint(4) DEFAULT 1 COMMENT '计费方式，0免费，1包月，2包年，默认为1',
pay_type tinyint(4) DEFAULT 1 COMMENT '支付类型，0全部，1真钱，2积分，默认为1',
money decimal(11,2) DEFAULT 0 COMMENT '积分价格',
real_money decimal(11,2) DEFAULT 0  COMMENT '真钱价格',
create_date datetime  COMMENT '创建时间',
update_date datetime  COMMENT '更新时间',
status tinyint(4) COMMENT '状态，0已下单，1已支付，2申请退款，3已退款，4拒绝退款,5已完成',
book_order_id int(11) COMMENT '书订单ID',
PRIMARY KEY (book_order_detail_id),
INDEX INDEX_BOOKORDERID (book_order_id) USING BTREE,
INDEX INDEX_STATUS (status) USING BTREE,
INDEX INDEX_CREATEDATE (create_date) USING BTREE,
INDEX INDEX_UPDATEDATE (update_date) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='书订单详情表';

#创建每日数据表 
CREATE TABLE daily_data_tb(
daily_data_id int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id',
pvs bigint(20) COMMENT 'pvs',
uvs bigint(20) COMMENT 'uvs',
ips bigint(20) COMMENT 'ips',
reading_number bigint(20) COMMENT '阅读数',
create_date datetime COMMENT '创建时间',
book_id int(11) COMMENT '书id外键',
acount_id int(11) COMMENT '账户id外键',
PRIMARY KEY (daily_data_id),
INDEX INDEX_CREATEDATE (create_date) USING BTREE,
INDEX INDEX_BOOKID (book_id) USING BTREE,
INDEX INDEX_ACOUNTID (acount_id) USING BTREE,
UNIQUE INDEX DAY_DATA (create_date,book_id,acount_id) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='每日数据表';

#创建数据表 
CREATE TABLE data_tb(
data_id int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id',
pvs bigint(20) COMMENT 'pvs',
uvs bigint(20) COMMENT 'uvs',
ips bigint(20) COMMENT 'ips',
reading_number bigint(20) COMMENT '阅读数',
create_date datetime COMMENT '创建时间',
book_id int(11) COMMENT '书id外键',
acount_id int(11) COMMENT '账户id外键',
PRIMARY KEY (data_id),
INDEX INDEX_CREATEDATE (create_date) USING BTREE,
INDEX INDEX_BOOKID (book_id) USING BTREE,
INDEX INDEX_ACOUNTID (acount_id) USING BTREE,
UNIQUE INDEX TIME_DATA (create_date,book_id,acount_id) USING BTREE
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='数据表';