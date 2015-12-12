package uk.co.baconi.secure.api.password;


import org.springframework.data.neo4j.repository.GraphRepository;
import uk.co.baconi.secure.api.user.User;

import java.util.List;

public interface PasswordRepository extends GraphRepository<Password> {

    List<Password> findByUsername(final String username);

    List<Password> findByWhereFor(final String whereFor);

}
