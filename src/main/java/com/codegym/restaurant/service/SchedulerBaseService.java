package com.codegym.restaurant.service;

import com.codegym.restaurant.model.hr.Schedule;

import java.util.List;

public interface SchedulerBaseService<T, ID> {
    List<T> getAll();

    T getById(ID id);

    T create(T t);

    T update(T t);

    void deleteById(ID id);
}
