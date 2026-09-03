package web.service;

import org.springframework.stereotype.Service;
import web.model.Car;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarServiceImp implements CarService {
    private List<Car> cars = new ArrayList<Car>();

    public CarServiceImp() {
        fiveCars();
    }

    public void fiveCars(){
        cars.add(new Car("BMW", "White", 456));
        cars.add(new Car("Toyota", "Black", 676));
        cars.add(new Car("Kia", "Grey", 786));
        cars.add(new Car("Mercedes", "Green", 546));
        cars.add(new Car("Tesla", "Pink", 656));
    }
    @Override
    public List<Car> countCars(int count) {
        if (cars.size()<=count){
            return cars;
        }
        return cars.subList(0,count);
    }
}
