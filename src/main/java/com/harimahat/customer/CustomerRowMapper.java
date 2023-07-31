package com.harimahat.customer;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author hari.mahat on 31.7.2023
 * Project learn-spring3

 */
@Component
public class CustomerRowMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Customer(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getInt("age"));
    }
}
