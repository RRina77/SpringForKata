package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.CreateUserDto;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController //контроллер будет возвращать данные в HTTP-ответе, например JSON, а не имена HTML-шаблонов
@RequestMapping("/api/users") //общий адрес для всех методов этого контроллера
public class UserRestController {
    private final UserService userService;

    public  UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping //метод будет обрабатывать GET-запрос по адресу /api/users
    public List<UserDto> getAllUsers() {
        return userService.listUsers().stream()
                .map(UserDto::new) //каждый объект User преобразуем в UserDto
                .collect(Collectors.toList()); //а потом обратно в список
        //Spring автоматически преобразует список DTO в JSON
    }


    @DeleteMapping("/{id}") //принимает DELETE-запрос с ID пользователя.
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) { // @PathVariable-извлекает ID из адреса.
        userService.deleteUser(id); //вызывает уже существующий метод удаления
        return ResponseEntity.noContent().build(); //возвращает HTTP-статус 204 No Content, то есть удаление выполнено, дополнительное содержимое ответа не требуется
    }

    @GetMapping("/{id}") //GET-запрос с ID в адресе
    public UserDto getUserById(@PathVariable Integer id) { //извлекает число 12 из адреса и передаёт его в параметр id
        User user = userService.getUserById(id); //получение юзера через сервис
        return new UserDto(user); //преобразование пользователя в UserDto
    }

    //принять JSON, передать их сервису для сохранения и сообщить браузеру, что пользователь создан
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto createUserDto) { //<Void> - не вернет данных //@RequestBody - взять данные из http
        User user = new User();
        //из DTO в User
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());
        user.setAge(createUserDto.getAge());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(createUserDto.getPassword());

        //передача пользователя в сервис
        userService.createUser(user, createUserDto.getRoleIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable Integer id,
            @RequestBody CreateUserDto dto) {

        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        userService.updateUser(id, user, dto.getRoleIds());

        return ResponseEntity.noContent().build();
    }


}
