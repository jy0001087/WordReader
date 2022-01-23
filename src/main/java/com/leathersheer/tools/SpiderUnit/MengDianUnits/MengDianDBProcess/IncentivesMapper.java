package com.leathersheer.tools.SpiderUnit.MengDianUnits.MengDianDBProcess;

import java.util.ArrayList;

public interface IncentivesMapper {
    ArrayList getIncentives();
    ArrayList getIncentivesWithData(String data);
    void commitColumn(IncentivesBean incentives);
}

