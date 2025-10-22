package com.project.rest_api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.project.rest_api.entity.Car;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarRepositoryTests {

    @Autowired
    private CarRepository carRepository;

    @Test
    public void carRepository_SaveCar_ReturnsSavedCar() {

        // Arrange
        Car car = new Car(null, "BMW-35H", 125, 2L);

        // Act
        Car savedCar = carRepository.save(car);

        // Assert
        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getId()).isGreaterThan(0);
        assertThat(savedCar.getModel()).isEqualTo("BMW-35H");
        assertThat(savedCar.getHorsepower()).isEqualTo(125);
        assertThat(savedCar.getOwnerId()).isGreaterThan(0);
    }

    @Test
    public void carRepository_GetAll_ReturnsMoreThanOneCar() {

        // Arrange
        Car car1 = new Car(null, "BMW-35H", 125, 2L);
        Car car2 = new Car(null, "BMW-39H", 145, 3L);

        // Act
        carRepository.save(car1);
        carRepository.save(car2);

        List<Car> carList = carRepository.findAll();

        // Assert
        assertThat(carList).isNotNull();
        assertThat(carList.size()).isEqualTo(2);
    }

    @Test
    public void carRepository_ClearAll_RemovesAllCarEntries() {
        // Arrange
        Car car1 = new Car(null, "BMW-35H", 125, 2L);
        Car car2 = new Car(null, "BMW-39H", 145, 3L);

        carRepository.save(car1);
        carRepository.save(car2);

        // Act
        carRepository.deleteAll();
        List<Car> checkThatDeleted = carRepository.findAll();

        // Assert
        assertThat(checkThatDeleted).isEmpty();

    }
}
