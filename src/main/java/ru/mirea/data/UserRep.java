package ru.mirea.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRep extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
