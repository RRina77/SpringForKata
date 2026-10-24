package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "User not found with username: " + username
            );
        }

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(int id) {
        return userRepository.findWithRolesById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Пользователь не найден: " + id
                        ));
    }

    @Transactional(readOnly = true)
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public void createUser(User user, Set<Integer> roleIds) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException(
                    "Пользователь с таким именем уже существует"
            );
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Введите пароль");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(findRoles(roleIds));

        userRepository.save(user);
    }

    @Transactional
    public void updateUser(int id, User formUser, Set<Integer> roleIds) {
        User existingUser = getUserById(id);

        User sameNameUser = userRepository.findByUsername(
                formUser.getUsername()
        );

        if (sameNameUser != null && sameNameUser.getId() != id) {
            throw new IllegalArgumentException(
                    "Это имя пользователя уже занято"
            );
        }

        existingUser.setUsername(formUser.getUsername());

        if (formUser.getPassword() != null
                && !formUser.getPassword().isBlank()) {
            existingUser.setPassword(
                    passwordEncoder.encode(formUser.getPassword())
            );
        }

        existingUser.setRoles(findRoles(roleIds));

        userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(int id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    private Set<Role> findRoles(Set<Integer> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "Выберите хотя бы одну роль"
            );
        }

        List<Role> roles = roleRepository.findAllById(roleIds);

        if (roles.size() != roleIds.size()) {
            throw new IllegalArgumentException("Выбрана неизвестная роль");
        }

        return new HashSet<>(roles);
    }
}