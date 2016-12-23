package uk.co.baconi.secure.base.common;

import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Deprecates all methods that use the built in ID's should.
 * <p>
 * This is because ID's can be reused, so should refrain from using them.
 *
 * @param <A> Neo4J Model Type
 */
public interface SmbGraphRepository<A> extends GraphRepository<A> {

    @Override
    @Deprecated
    A findOne(Long aLong);

    @Override
    @Deprecated
    A findOne(Long id, int depth);

    @Override
    @Deprecated
    boolean exists(Long aLong);

    @Override
    @Deprecated
    void delete(Long aLong);

    @Override
    @Deprecated
    Iterable<A> findAll(Iterable<Long> ids);

    @Override
    @Deprecated
    Iterable<A> findAll(Iterable<Long> ids, int depth);

    @Override
    @Deprecated
    Iterable<A> findAll(Iterable<Long> ids, Sort sort);

    @Override
    @Deprecated
    Iterable<A> findAll(Iterable<Long> ids, Sort sort, int depth);

}
