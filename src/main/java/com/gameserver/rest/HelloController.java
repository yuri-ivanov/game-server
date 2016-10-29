package com.gameserver.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class HelloController {
    /*
    @Autowired
    private DataSource ds;
    */

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/dbtest")
    public String dbTest() throws SQLException {
        StringBuffer buffer = new StringBuffer();
        jdbcTemplate.query("select * from test", (rs, rowNum)-> (rs.getString("text")) ).forEach(text -> buffer.append(text) );
        return buffer.toString();
    }

}
