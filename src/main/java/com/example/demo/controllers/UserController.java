package com.example.demo.controllers;

import jakarta.validation.Valid;

import org.apache.coyote.Response;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    Map<String , Object> response = new HashMap<>();

    /**
     * Metodo para obtener un listado de todos los usuarios registrados
     * @return todos los usuarios registrados
     */
    @GetMapping(value = "/all")
    public ResponseEntity<Map<String , Object>> getAllUsers() {
        response.clear();

        ArrayList<User> users = userRepository.findAllBy();

        response.put("users" , users);

        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    /**
     * Metodo para generar usuarios a mano
     */
    private void generarUsuarios() {
        // Aqui vamos a crear dos usuarios, para hacer pruebas
        User user1 = new User("Jorge" , "adrian@ujaen.es" , "12345679" );

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user1.getPassword());
        user1.setPassword(hashedPassword);

        // Ahora aqui tenemos que generarle el token
        user1.setAuthToken(generarApi_Token());
//        user1.setUserGroup("MARKETING");

        userRepository.save(user1);
    }

    /**
     * Metodo para obtener verificar el login del usuario
     * @param authToken
     * @return
     */
    @GetMapping(value = "/status")
    public ResponseEntity<Boolean> checkStatus(@RequestHeader(value = "Authorization") String authToken) {

        if(authToken == null || authToken.isEmpty()) {
            return new ResponseEntity<>(false , HttpStatus.UNAUTHORIZED);
        }

        User tempUser = userRepository.findByAuthToken(authToken);

        if(tempUser != null && tempUser.getUserGroup() != null && tempUser.getUserGroup().equals("MARKETING")) {
            // Entonces va bien la cosa
            return new ResponseEntity<>(true , HttpStatus.OK);
        }

        return new ResponseEntity<>(false , HttpStatus.UNAUTHORIZED);
    }

    /**
     * Metodo para realizar el login de un usuario, es necesario que tenga datos validos
     * @param user
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Map<String , Object>> login(@RequestBody User user) {
        response.clear();

        // Si algun campo no se ha introducido , mandamos respuesta con el error
        if (user.getName() == null || user.getName().trim().isEmpty() || user.getPassword() == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Nombre de usuario y contraseña son campos obligatorios.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        User tempUser = userRepository.getUserByName(user.getName());

        boolean credentialsInvalid = true;

        if(tempUser != null ) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(user.getPassword(), tempUser.getPassword())) {
                System.out.println(user.getPassword() + " --- " + tempUser.getPassword());

                credentialsInvalid = false;
            }
        }

        if(credentialsInvalid || tempUser.getUserGroup() == null || !tempUser.getUserGroup().equals("MARKETING")) {
            // Entonces tenemos que rechazar el acceso
            response.put("mensaje" , "Nombre de usuario o contraseña incorrectos ");

            return new ResponseEntity<>(response , HttpStatus.UNAUTHORIZED);
        }

        // Si llega aqui entonces esta correcto
        response.put("user" , tempUser);
        response.put("mensaje" , "Login realizado con éxito");

        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    //    @PutMapping(value = "/update")
//    public ResponseEntity<Map<String , Object>> update(@RequestBody @NotNull User userUpdate) {
//        response.clear();
//
//        User tempUser = userRepository.findByAuthToken(userUpdate.getAuthToken());
//
//        if(tempUser == null) {
//            response.put("mensaje" , "API token incorrecta");
//            return new ResponseEntity<>(response , HttpStatus.UNAUTHORIZED);
//        }
//
//        if(userUpdate == null) {
//            response.put("mensaje" , "No hay body en la petición");
//            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
//        }
//
//
//        if(userUpdate.getEmail() != null && userUpdate.getEmail().isEmpty()) {
//            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,6}$";
//            Pattern pattern = Pattern.compile(emailRegex);
//            Matcher matcher = pattern.matcher(userUpdate.getEmail());
//
//            if(!matcher.matches()) {
//                response.put("mensaje" , "El email no tiene un formato válido");
//                response.put("status" , HttpStatus.BAD_REQUEST.value());
//                return ResponseEntity.badRequest().body(response);
//            }
//
//            if(!userUpdate.getEmail().equals(tempUser.getEmail()) && userRepository.getUserByEmail(userUpdate.getEmail()) != null) {
//                response.put("mensaje" , "El email ya esta registrado");
//                response.put("status" , HttpStatus.CONFLICT.value());
//
//                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
//            }
//
//            tempUser.setEmail(userUpdate.getEmail());
//        }
//
//        if (userUpdate.getPassword() != null && !userUpdate.getPassword().isEmpty()) {
//
//            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            String hashedPassword = passwordEncoder.encode(userUpdate.getPassword());
//            tempUser.setPassword(hashedPassword);
//        }
//
//        if (userUpdate.getName() != null && !userUpdate.getName().isEmpty()) {
//            tempUser.setName(userUpdate.getName());
//        }
//
//        userRepository.save(tempUser);
//
//        response.put("mensaje" , "Usuario actualizado correctamnte");
//        response.put("user" , tempUser);
//        response.put("status" , HttpStatus.OK.value());
//
//        return ResponseEntity.ok(response);
//    }



//    /**
//     * Metodo para obtener un usuario a traves de su mail
//     * @param email
//     * @return
//     */
//    @GetMapping(value ="/{email}")
//    public ResponseEntity<Map<String, Object>> getUserByEmail(@PathVariable String email){
//        response.clear();
//
//        User requestedUser = userRepository.getUserByEmail(email);
//
//        if(requestedUser == null) {
//            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
//        }
//
//        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,6}$";
//        Pattern pattern = Pattern.compile(emailRegex);
//        Matcher matcher = pattern.matcher(requestedUser.getEmail());
//
//        if (!matcher.matches()) {
//            response.put("message", "El email no tiene un formato válido");
//            response.put("status", HttpStatus.BAD_REQUEST.value());
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        response.put("user" , requestedUser);
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }


//    @PostMapping(value = "/addUser")
//    public ResponseEntity<Map<String , Object>> addUser(@Valid @RequestBody User user) {
//        response.clear();
//
//        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,6}$";
//        Pattern pattern = Pattern.compile(emailRegex);
//        Matcher matcher = pattern.matcher(user.getEmail());
//
//        if(!matcher.matches()) {
//            response.put("mensaje" , "El email no tiene un formato válido");
//            response.put("status" , HttpStatus.BAD_REQUEST.value());
//
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String hashedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(hashedPassword);
//
//        if(user.getUserGroup() == null || user.getUserGroup().equals("")) {
//            user.setUserGroup("USER");
//        }
//
//        if(userRepository.getUserByEmail(user.getEmail()) == null) {
//            // Si no lo encuentra, va bien la cosa, lo guardamos
//            user.setAuthToken(generarApi_Token());
//            userRepository.save(user);
//            response.put("mensaje" , "Usuario creado correctamente, tu API key es " + user.getAuthToken());
//            response.put("status" , 200);
//
//            return ResponseEntity.ok(response);
//        }
//
//        // En caso de que ya este registrado
//        response.put("mensaje" , "Usuario ya registrado");
//        response.put("status" , HttpStatus.UNAUTHORIZED);
//
//        return new ResponseEntity<>(response , HttpStatus.UNAUTHORIZED);
//    }

    // Para generar el token que se le dara a cada usuario
    private String generarApi_Token() {
        return UUID.randomUUID().toString();
    }
}