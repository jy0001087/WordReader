package com.leathersheer.tools.EmploymentUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/CustomerDataProcesser" ,name="CustomerDataProcesser")
public class CustomerDataProcesser extends HttpServlet {
    public static final Logger CustomerDPL= LogManager.getLogger();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomerDPL.info("CustomerDataProcesser is triggered!");
    }

    public void SQLExecuter(){

    }
}
