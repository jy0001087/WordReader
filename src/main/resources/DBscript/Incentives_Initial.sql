-- public.incentives definition
-- DROP TABLE public.incentives;

CREATE TABLE public.incentives (
                                  payment_days varchar(50) NULL,   --结算周期
                                  CP_code varchar(10) NOT NULL,    --CP代码
                                  CP_name varchar(50) NULL,        --CP名称
                                  settle_rate varchar(15) NULL,    --结算比率
                                  settle_amount int4 NULL         --结算金额
                                  -- CP代号不能为空
);


SELECT * FROM incentives;


INSERT INTO public.incentives (payment_days,CP_code,CP_name,settle_rate,settle_amount)
VALUES ('202101','630222','四川萌点科技','0.05',215235);