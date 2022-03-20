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