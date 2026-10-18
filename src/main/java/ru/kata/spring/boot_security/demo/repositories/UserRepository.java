package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(attributePaths = "roles")
    User findByUsername(String username);

    @Override
    @EntityGraph(attributePaths = "roles")
    List<User> findAll();

    @EntityGraph(attributePaths = "roles")
    Optional<User> findWithRolesById(Integer id);
}