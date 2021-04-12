package com.codegym.restaurant.service;

import java.util.List;

public interface BaseService<G, ID> {

    List<G> getAll();

    List<G> getAllDeleted();

    G getById(ID id);

    G create(G g);

    G update(G g);

    void delete(ID id);

    void restore(ID id);
}
