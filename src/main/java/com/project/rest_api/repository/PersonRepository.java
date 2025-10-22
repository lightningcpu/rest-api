package com.project.rest_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.rest_api.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
