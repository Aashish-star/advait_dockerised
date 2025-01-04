package org.advait.assignment.repository;

import java.util.Optional;

import org.advait.assignment.entity.UserDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDetailEntity, Long> {

	Optional<UserDetailEntity> findByEmail(String email);

	Optional<UserDetailEntity> findByUserName(String userName);
}
