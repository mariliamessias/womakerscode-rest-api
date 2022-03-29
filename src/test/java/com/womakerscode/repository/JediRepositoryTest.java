package com.womakerscode.repository;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.womakerscode.model.Jedi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
public class JediRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JediRepository jediRepository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return All Jedis with success from repository")
    public void testFindAllSuccess() {
        // execucao
        List<Jedi> jedis = jediRepository.findAll();

        // assert
        Assertions.assertEquals(3, jedis.size(), "We should have 3 jedis in our database");
    }

    @Test
    @DataSet("empty-jedi.yml")
    @DisplayName("Should return empty list of Jedis with success from repository")
    public void testFindAllEmpty() {
        // execucao
        List<Jedi> jedis = jediRepository.findAll();

        // assert
        Assertions.assertEquals(0, jedis.size(), "We should have 0 jedis in our database");
    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return a Jedi with success from repository")
    public void testFindByIdSuccess() {

        Jedi expectedJedi = new Jedi(1, "HanSolo", 50, 1);

        // execucao
        Optional<Jedi> jedi = jediRepository.findById(1);

        // assert
        Assertions.assertTrue(jedi.isPresent(), "Jedi was not found");
        Assertions.assertEquals(jedi.get().getVersion(), expectedJedi.getVersion(), "Jedis version must be the same");
        Assertions.assertEquals(jedi.get().getName(), expectedJedi.getName(), "Jedis name must be the same");
        Assertions.assertEquals(jedi.get().getStrength(), expectedJedi.getStrength(), "Jedis strength must be the same");
        Assertions.assertEquals(jedi.get().getId(), expectedJedi.getId(), "Jedis id must be the same");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should not return a Jedi when not found in repository")
    public void testFindByIdNotFound() {
        // execucao
        Optional<Jedi> jedi = jediRepository.findById(55);

        // assert
        Assertions.assertFalse(jedi.isPresent(), "Jedi was not found");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should save a Jedi with success into repository")
    public void testSaveSuccess() {
        Jedi expectedJedi = new Jedi(4, "Leia", 150, 1);
        Jedi jedi = new Jedi("Leia", 150, 1);

        // execucao
        Jedi jediSaved = jediRepository.save(jedi);

        // assert
        Assertions.assertNotNull(jediSaved, "Jedis should not be null");
        Assertions.assertEquals(jediSaved.getVersion(), expectedJedi.getVersion(), "Jedis version must be the same");
        Assertions.assertEquals(jediSaved.getName(), expectedJedi.getName(), "Jedis name must be the same");
        Assertions.assertEquals(jediSaved.getStrength(), expectedJedi.getStrength(), "Jedis strength must be the same");
        Assertions.assertEquals(jediSaved.getId(), expectedJedi.getId(), "Jedis id must be the same");
    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should not update a Jedi when doesn't exist in repository")
    public void testUpdateFail() {
        Jedi jedi = new Jedi(5, "Leia", 350, 1);

        // execucao
        boolean result = jediRepository.update(5, jedi);

        // assert
        Assertions.assertFalse(result, "Jedi not updated");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should delete a Jedi with success into repository")
    public void testDeleteSuccess() {

        // execucao
        boolean result = jediRepository.delete(1);

        // assert
        Assertions.assertTrue(result, "Jedi not deleted");
        List<Jedi> jedis = jediRepository.findAll();
        Assertions.assertEquals(2, jedis.size(), "We should have 2 jedis in our database");
    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should not delete a Jedi when doesn't exist in repository")
    public void testDeleteFail() {

        // execucao
        boolean result = jediRepository.delete(5);

        // assert
        Assertions.assertFalse(result, "Jedi not deleted");
        List<Jedi> jedis = jediRepository.findAll();
        Assertions.assertEquals(3, jedis.size(), "We should have 3 jedis in our database");
    }
}
