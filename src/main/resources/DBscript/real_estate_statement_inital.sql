-- public.realestatestatement definition
-- DROP TABLE public.realestatestatement;

CREATE TABLE public.realestatestatement (
                                  area_name varchar(50) NULL,   --数据区域
                                  total_area_proportion varchar(50)  NULL,    --区域售出总面积
                                  residiential_house_num varchar(50) NULL,        --区域住宅售出数
                                  residiential_house_proportion varchar(50) NULL,    --区域住宅售出面积
                                  not_residiential_house_proportion varchar(50) null,         --区域非住宅出售面积
                                  city_name varchar(20) null,
                                  record_time timestamp
);


create table public.resoldhouses(
    houseid varchar(50)  NULL,     --从url里分离出来的ID
    proportion decimal(20,5) null,   --面积
    totalPrice  decimal(20,5) null,
    unitPrice  decimal(20,5) null,
    houseType varchar(50)  NULL,    --户型
    mansion varchar(50)  NULL,  -- 楼型
    floor varchar(50)  NULL,
    orientation varchar(50)  NULL, --朝向
    isSold varchar(50)  NULL,      --已售标志
    houseUrl varchar(50)  NULL,
    age varchar(50)  NULL,      --房龄
    grabTime timestamp ,  --抓取时间
    source varchar(50)  NULL    --信息来源网站
)