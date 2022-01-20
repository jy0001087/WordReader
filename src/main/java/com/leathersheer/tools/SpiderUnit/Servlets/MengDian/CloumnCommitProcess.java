package com.leathersheer.tools.SpiderUnit.Servlets.MengDian;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "CloumnCommitProcessServlet", urlPatterns = "/CloumnCommitProcess")
public class CloumnCommitProcess extends HttpServlet {
    public static final Logger CommitLogger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CommitLogger.debug("enter cloumnCommitPorcess");
        String insertdata = req.getParameter("insertdata");
        CommitLogger.debug("insertdata==" + insertdata);
        ArrayList<IncentivesBean> commitCloumns = getCloumnBean(insertdata);
        commitCloumToDB(commitCloumns);
    }

    private ArrayList<IncentivesBean> getCloumnBean(String insertdata) {
        ArrayList<IncentivesBean> commitCloumnsArray = new ArrayList<IncentivesBean>();
        String[] commitCloumnsStringArray = insertdata.split(",");
        IncentivesBean newIncentive = new IncentivesBean();
        for (int i = 0; i < commitCloumnsStringArray.length; i++) {
            if (i % 5 == 0) {
                newIncentive = new IncentivesBean();
            }
            //清洗数据start
            commitCloumnsStringArray[i] = commitCloumnsStringArray[i].trim();
            Pattern numberp = Pattern.compile("[^0-9]");
            int j = i / 5;
            int temp = (i - 5 * j) % 5;
            CommitLogger.debug("当前处理数据 i=" + i + " j=" + j +" temp=" + temp + " 数据：" + commitCloumnsStringArray[i]);
            switch (temp) {
                case 0:
                    Matcher m = numberp.matcher(commitCloumnsStringArray[i]);
                    newIncentive.payment_days = m.replaceAll("").trim();
                    break;
                case 1:
                    newIncentive.CP_code = commitCloumnsStringArray[i];
                    break;
                case 2:
                    newIncentive.CP_name = commitCloumnsStringArray[i];
                    break;
                case 3:
                    newIncentive.settle_rate = commitCloumnsStringArray[i];
                    break;
                case 4:
                    Matcher q = numberp.matcher(commitCloumnsStringArray[i]);
                    newIncentive.settle_amount = Integer.parseInt(q.replaceAll("").trim());
                    break;
            }
        }
        commitCloumnsArray.add(newIncentive);
        return commitCloumnsArray;
    }

    public void commitCloumToDB(ArrayList<IncentivesBean> cloumnData) {
        DBTools db = new DBTools();

        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            IncentivesMapper mapper = sqlsession.getMapper(IncentivesMapper.class);

            try {
                db.dblogger.info("开始插入数据");
                for (IncentivesBean cloumnBean : cloumnData) {
                    mapper.commitColumn(cloumnBean);
                }
            } catch (Exception e) {
                db.dblogger.error("数据插入异常：");
                db.dblogger.error(e.toString(), e);
            }
            sqlsession.commit();
        }
    }

    public static void main(String[] args) {
        System.out.println(Integer.parseInt("5656565656"));
    }
}
