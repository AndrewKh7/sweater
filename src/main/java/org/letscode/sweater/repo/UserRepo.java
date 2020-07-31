package org.letscode.sweater.repo;

import org.letscode.sweater.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
