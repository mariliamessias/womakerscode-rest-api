package com.womakerscode.service;

import com.womakerscode.model.Jedi;
import com.womakerscode.repository.JediRepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JediService {

    private static final Logger logger = LogManager.getLogger(JediService.class);
    private final JediRepositoryImpl jediRepositoryImpl;

    public JediService(JediRepositoryImpl jediRepositoryImpl) {
        this.jediRepositoryImpl = jediRepositoryImpl;
    }

    public Optional<Jedi> findById(int id) {
        logger.info("Find Jedi with id: {}", id);
        return jediRepositoryImpl.findById(id);
    }

    public List<Jedi> findAll() {
        logger.info("Bring all the Jedis from the Galaxy");
        return jediRepositoryImpl.findAll();
    }

    public Jedi save(Jedi jedi) {
        jedi.setVersion(1);
        logger.info("Update Jedi from system");
        return jediRepositoryImpl.save(jedi);
    }

    public boolean update(int id, Jedi jedi) {
        logger.info("Update Jedi from system");
        return jediRepositoryImpl.findById(id)
                .map(result -> jediRepositoryImpl.update(jedi))
                .orElse(false);
    }

    public boolean delete(int id) {
        logger.info("Remove Jedi from system");
        return jediRepositoryImpl.findById(id)
                .map(result -> jediRepositoryImpl.delete(id))
                .orElse(false);
    }
}
