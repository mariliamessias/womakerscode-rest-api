package com.womakerscode.service;

import com.womakerscode.model.Jedi;
import com.womakerscode.repository.JediRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JediServiceTest {

    @Autowired
    private JediService jediService;

    @MockBean
    private JediRepositoryImpl jediRepository;


    @Test
    @DisplayName("Should return Jedi By Id with success")
    public void testFindByIdSuccess() {

        // cenario
        Jedi mockJedi = new Jedi(1, "Jedi Name", 10, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediRepository).findById(1);

        // execucao
        Optional<Jedi> returnedJedi = jediService.findById(1);

        // assert
        Assertions.assertTrue(returnedJedi.isPresent(), "Jedi was not found");
        Assertions.assertSame(returnedJedi.get(), mockJedi, "Jedis must be the same");
    }

    @Test
    @DisplayName("Should return empty result when Jedi is not found")
    public void testFindByIdNotFound() {

        // cenario
        Mockito.doReturn(Optional.empty()).when(jediRepository).findById(1);

        // execucao
        Optional<Jedi> returnedJedi = jediService.findById(1);

        // assert
        Assertions.assertFalse(returnedJedi.isPresent(), "Jedi was not found");
    }

    @Test
    @DisplayName("Should return All Jedis with success")
    public void testFindAllSuccess() {

        // cenario
        Jedi mockJedi = new Jedi(1, "Jedi Name", 10, 1);
        Mockito.doReturn(List.of(mockJedi)).when(jediRepository).findAll();

        // execucao
        List<Jedi> returnedJedi = jediService.findAll();

        // assert
        Assertions.assertFalse(returnedJedi.isEmpty(), "No Jedi was found");
        Assertions.assertSame(returnedJedi.get(0), mockJedi, "Jedis must be the same");
    }

    @Test
    @DisplayName("Should return empty list of result when Jedis are not found")
    public void testFindAllNotFound() {

        // cenario
        Mockito.doReturn(List.of()).when(jediRepository).findAll();

        // execucao
        List<Jedi> returnedJedi = jediService.findAll();

        // assert
        Assertions.assertTrue(returnedJedi.isEmpty(), "No Jedi was found");
    }
}
