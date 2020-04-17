package client.controller.rest;

import client.model.User;
import client.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/user")
@RestController
public class AdminRestController {
    private UserServiceImpl userServiceImpl;

    @Autowired
    public AdminRestController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userServiceImpl.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        return ResponseEntity.ok(userServiceImpl.getById(id));
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userServiceImpl.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        if (userServiceImpl.isExist(id)) {
            userServiceImpl.deleteById(id);
            return ResponseEntity.ok("User with id = " + id + " was deleted");
        } else {
            return ResponseEntity.ok("User with id = " + id + " doesn't exist");
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userServiceImpl.update(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
