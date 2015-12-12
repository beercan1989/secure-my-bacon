package uk.co.baconi.secure.api.user;


import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface UserRepository extends GraphRepository<User> {

    List<User> findByName(final String name);

}
