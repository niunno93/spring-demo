package it.ovi.demo.repositories;

import it.ovi.demo.entities.UserDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetailEntity, Long> {

    Optional<UserDetailEntity> findByEmail(String email);
}
