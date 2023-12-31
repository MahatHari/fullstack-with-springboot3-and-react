package com.harimahat.customer;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author hari.mahat on 31.7.2023
 * Project learn-spring3
 */
@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper=customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql= """
                SELECT id, name, email, age FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Long customerId) {
        var sql= """
                   SELECT id, name, email, age FROM customer WHERE id=?
                """;

        return jdbcTemplate.query(sql, customerRowMapper, customerId).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) throws DataAccessException {
        var sql = """
                INSERT INTO customer(name, email, age) VALUES (?, ?, ?)    \s
                """;
     int update=   jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
        System.out.println("JDBC Template update ="+update);

    }

    @Override
    public void deleteCustomerById(Long customerId) {

        var sql= """
                DELETE FROM customer WHERE id = ?
                """;
        int result= jdbcTemplate.update(sql, customerId);
        System.out.printf("Deleted Customer By Id %s and count is %d", customerId, result);
    }

    @Override
    public void updateCustomer(Customer update) {
             if(update.getName()!=null){
                 String sql= "UPDATE customer SET name = ? where id = ?";
                 int result= jdbcTemplate.update(sql, update.getName(), update.getId());
                 System.out.println("updated customer name result = "+result);
             }
             if(update.getEmail()!=null){
                 String sql= "UPDATE customer SET email = ? where id = ?";
                 int result= jdbcTemplate.update(sql, update.getEmail(), update.getId());
                 System.out.println("updated customer email result = "+result);
             }
            if(update.getAge()!=null){
                String sql= "UPDATE customer SET age = ? where id = ?";
                int result= jdbcTemplate.update(sql, update.getAge(), update.getId());
                System.out.println("updated customer age result = "+result);
            }

    }

    @Override
    public boolean existPersonWithEmail(String emailcustomer) {

        var sql= """
                SELECT count(id) FROM customer WHERE email=?
                """;
        Integer count= jdbcTemplate.queryForObject(sql,Integer.class, emailcustomer);
        return count!=null && count>0;
    }

    @Override
    public boolean existPersonWithId(Long customerId) {
        var sql= """
                SELECT count(id) FROM customer WHERE id=?
                """;
        Integer count= jdbcTemplate.queryForObject(sql,Integer.class, customerId);
        return count!=null && count>0;
    }
}
