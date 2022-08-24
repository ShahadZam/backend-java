package com.example.rentaperson.repository;

import com.example.rentaperson.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


    List<User> findUsersByCity(String city);

    User findUsersByUsername(String username);
}
