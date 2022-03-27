select count(*) from houseinfo;

select * from houseinfo h ;

select * from houseinfo where house_code ='BJ2547146070211960832'

select * from houseinfo order by price ;

select * from houseinfo  order by update_timestamp  desc

truncate table houseinfo;

insert into resoldhouses(houseid,proportion,totalPrice,unitPrice,houseType,mansion,floor,orientation,isSold,houseUrl,age,grabTime,source) 
values(106110637050, 74.66, 140.0, 18752.0, 2室1厅,  中楼层(共25层) , null,  东 , false,
https://cd.lianjia.com/ershoufang/106110637050.html,  板塔结合, 2022-03-27 19:21:25.806, cd.lianjia.com);