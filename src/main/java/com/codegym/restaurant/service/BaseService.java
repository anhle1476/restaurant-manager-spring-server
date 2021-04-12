package com.codegym.restaurant.service;

import java.util.List;

public interface BaseService<G, ID> {

    List<G> getAll();

    List<G> getAllDeleted();

    G getById(ID id);

    void create(G g);

    void update(G g);

    void delete(ID id);

    void Restore(ID id);
}
