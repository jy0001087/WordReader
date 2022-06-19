package com.leathersheer.tools.AndroidResponse.DataFragment;

import com.leathersheer.tools.SpiderUnit.LianJiaCD.LianjiaCDBean;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ChengDuHouseBean;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DataServletMapper {
    public List<LianjiaCDBean> getHouseInfoAll();

    public List<LianjiaCDBean> getHouseHist();

    public List<ChengDuHouseBean> getRealEstateInfo();
}
