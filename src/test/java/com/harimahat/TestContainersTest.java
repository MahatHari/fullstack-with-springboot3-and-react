package com.harimahat;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author hari.mahat on 31.7.2023
 * Project learn-spring3
 */


public class TestContainersTest extends AbstractTestContainer {

    @Test
    void canStartPostgresDB(){
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }


}
