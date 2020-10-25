package com.caspco.unittest.file;

import com.caspco.unittest.runner.CaspianSpringRunner;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@RunWith(CaspianSpringRunner.class)
@ContextConfiguration
public class EmptySpringTest {
    //TODO from davood akbari: Write a test for the commented item
//    @MockModel
//    private SampleModel emptySampleModel;

    @Configuration
    static class Config {

        @Bean
        public Gson gson() {
            return new Gson();
        }
    }

    @Test
    public void test() {

    }
}
