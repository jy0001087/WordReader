package com.leathersheer.tools.SpiderUnit.DBUnits;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBTools {
    public static Logger dblogger = LogManager.getLogger();
    String mybatisconf = "mybatis-config.xml";

    public SqlSessionFactory getSqlSession() {
        SqlSessionFactory sqlsession = null;
        try {
            InputStream in = Resources.getResourceAsStream(mybatisconf);
            sqlsession = new SqlSessionFactoryBuilder().build(in);
        } catch (Exception e) {
            dblogger.error("读取mybatis-config.xml失败！！");
             dblogger.error(e.toString(),e);
        }
        return sqlsession;
    }

    public static void main(String[] args) {
        DBTools db = new DBTools();
        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            DBUnitsMapper mapper = sqlsession.getMapper(DBUnitsMapper.class);
            dblogger.debug("数据库连接状态："+mapper.conntest());
        } catch (Exception e) {
            dblogger.error("数据库操作异常！！");
            dblogger.error(e.toString(),e);
        }
    }
}