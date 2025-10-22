package com.project.rest_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.project.rest_api.entity.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByOwnerId(Long ownerId);

    @Query("SELECT COUNT(DISTINCT(model)) FROM Car")
    Long countDistinctModels();
}
