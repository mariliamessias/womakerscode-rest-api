package com.womakerscode.repository;

import com.womakerscode.model.Jedi;

import java.util.List;
import java.util.Optional;

public interface JediRepository {

    Optional<Jedi> findById(Integer id);
    List<Jedi> findAll();
    Jedi save(Jedi jedi);
    boolean update(Jedi jedi);
    boolean delete(Integer id);
}
