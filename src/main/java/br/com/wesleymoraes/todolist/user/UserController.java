package br.com.wesleymoraes.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        UserModel userName = this.userRepository.findByUserName(userModel.getUserName());

        if (userName != null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        }

        String passwordHashed = BCrypt.withDefaults()
                .hashToString((12), userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashed);

        UserModel userCreated = this.userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);

    }

}
