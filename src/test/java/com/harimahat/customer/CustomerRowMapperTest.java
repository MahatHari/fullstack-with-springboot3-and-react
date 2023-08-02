package com.harimahat.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @Author hari.mahat on 2.8.2023
 * Project learn-spring3
 */
class CustomerRowMapperTest {


    @Test
    void mapRow() throws SQLException {
        // GIVEN
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

       ResultSet resultSet=  mock(ResultSet.class);

       when(resultSet.getLong("id")).thenReturn(1L);
       when(resultSet.getString("name")).thenReturn("Hari");
        when(resultSet.getString("email")).thenReturn("example@example.com");
        when(resultSet.getInt( "age")).thenReturn(23);

        // WHEN
        Customer actual = customerRowMapper.mapRow(resultSet,1 );

        // THEN
        Customer expected= new Customer(
                1L, "Hari", "example@example.com", 23
        );

        assert actual != null;
        assertThat(actual.getId()).isEqualTo(expected.getId());

        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());

    }
}