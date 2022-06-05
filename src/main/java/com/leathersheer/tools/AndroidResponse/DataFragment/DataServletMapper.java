package com.leathersheer.tools.AndroidResponse.DataFragment;

import com.leathersheer.tools.SpiderUnit.LianJiaCD.LianjiaCDBean;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DataServletMapper {
    public List<LianjiaCDBean> getHouseInfoAll();
}
