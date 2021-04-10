package com.codegym.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface JpaSoftDeleteRepository<T, ID> extends JpaRepository<T, ID> {

    @Query("select e from #{#entityName} e where e.deleted=false")
    List<T> findAllAvailable();

    @Query("select e from #{#entityName} e where e.deleted=false and e.id=?1")
    Optional<T> findAvailableById(ID id);

    @Query("select e from #{#entityName} e where e.deleted=true")
    List<T> findAllDeleted();

    @Query("update #{#entityName} e set e.deleted=true where e.id=?1")
    @Modifying
    Integer softDelete(ID id);

    @Query("update #{#entityName} e set e.deleted=true where e.id in (?1)")
    @Modifying
    Integer softDeleteAll(Collection<ID> ids);

    @Query("update #{#entityName} e set e.deleted=false where e.id=?1")
    @Modifying
    Integer restore(ID id);

    @Query("update #{#entityName} e set e.deleted=false where e.id in (?1)")
    @Modifying
    Integer restoreAll(Collection<ID> ids);
}
