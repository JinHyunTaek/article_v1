package com.example.article.exception;

import com.example.article.api.error.BasicErrorCode;
import com.example.article.api.error.BasicException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExceptionTest {

    @Test
    public void throwEx() {
        throw new RuntimeException("ex");
    }
}
