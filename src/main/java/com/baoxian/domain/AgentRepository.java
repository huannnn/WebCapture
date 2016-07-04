package com.baoxian.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Repository
public class AgentRepository {

    @Autowired
    private LocalContainerEntityManagerFactoryBean factoryBean;

    private Connection getConnection() throws Exception {
        return factoryBean.getDataSource().getConnection();
    }

    public void insert(Agent agent) {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement pst = con.prepareStatement("insert into wc_agent(name,sex,phone,wechat,location,company,business,introduce,desc_Url) values (?,?,?,?,?,?,?,?,?)");
            pst.setString(1, agent.getName());
            pst.setString(2, agent.getSex());
            pst.setString(3, agent.getPhone());
            pst.setString(4, agent.getWechat());
            pst.setString(5, agent.getLocation());
            pst.setString(6, agent.getCompany());
            pst.setString(7, agent.getBusiness());
            pst.setString(8, agent.getIntroduce());
            pst.setString(9, agent.getDescUrl());
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
