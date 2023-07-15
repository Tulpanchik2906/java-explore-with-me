package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Hit;

public interface HitRepository extends JpaRepository<Hit, Long>, QuerydslPredicateExecutor<Hit> {
    public long countByUri(String uri);

    @Query("select count(distinct(hit.ip)) " +
            "FROM Hit as hit " +
            "where hit.uri = ?1")
    public long countDistinctIpByUri(String uri);
}
