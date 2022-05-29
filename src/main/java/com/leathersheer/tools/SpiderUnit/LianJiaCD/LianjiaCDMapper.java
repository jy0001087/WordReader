package com.leathersheer.tools.SpiderUnit.LianJiaCD;

import java.util.ArrayList;

public interface LianjiaCDMapper {
    void insertColumn(LianjiaCDBean houseBean);
    void insertColumnHist(LianjiaCDBean houseBean);
    void updateOnlyWithDate(LianjiaCDBean houseBean);
    void updateWithPriceAndFollowinfo(LianjiaCDBean houseBean);
    LianjiaCDBean selectHouseState(LianjiaCDBean houseBean);
}
