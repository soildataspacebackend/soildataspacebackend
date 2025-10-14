package com.example.demo.controllers;

import com.example.demo.models.News;
import com.example.demo.models.User;
import com.example.demo.repositories.NewsRepository;

import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.IdRequest;
import com.example.demo.utils.PayloadRequestNew;
import com.example.demo.utils.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1/news")
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;
    Map<String , Object> response = new HashMap<>();

    /**
     * Metodo para obtener el listado de noticias
     * @return
     */
    @GetMapping(value = "/all")
    public ResponseEntity<Map<String , Object>> getAllNews() {
        response.clear();

        ArrayList<News> news = newsRepository.findAllBy();

        response.put("news" , news);

        return new ResponseEntity<>(response , HttpStatus.OK);
    }


    /**
     * Metodo para obtener una noticia por su id
     * @param request @return
     */
    @GetMapping(value = "/id")
    public ResponseEntity<Map<String, Object>> getNewBy(@RequestBody IdRequest request){
        response.clear();

        News requestedNew = newsRepository.getNewsById(request.getId());

        if(requestedNew == null) {
            response.put("mensaje", "No hay noticias con ese ID");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        response.put("new" , requestedNew);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Metodo para permitir al usuario de noticias, crear nuevas noticias
     * @param payloadRequestNew
     * @return
     */
    @PostMapping(value = "/addNew")
    public ResponseEntity<Map<String , Object>> addNew(@RequestBody PayloadRequestNew payloadRequestNew) {
        response.clear();

        News newsRequest = payloadRequestNew.getNews();
        String authToken = payloadRequestNew.getAuthTokenId();

        if (authToken.trim().isEmpty()){
            response.put("mensaje", "No autorizado");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        User temp = userRepository.findByAuthToken(authToken);
        if (temp == null || !temp.getUserGroup().equals("MARKETING")) {
            response.put("mensaje", "No autorizado");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if(newsRequest.NotValid()) {
            response.put("mensaje" , "El formato de la noticia no es válido");
            response.put("status" , HttpStatus.CONFLICT);
            return new ResponseEntity<>(response , HttpStatus.CONFLICT);
        }

        // En caso de que si vaya bien, entonces seguimos, puede hacer la noticia
        News savedNew = newsRepository.save(newsRequest);

        response.put("mensaje" , "Se ha creado la noticia con éxito");
        response.put("id" , savedNew.getId());

        return ResponseEntity.ok().body(response);
    }


    /**
     * Metodo para borrar una noticia que se solicite, es necesario tener autorización
     * @param deleteNewRequestId
     * @param authTokenInfo
     * @return
     */
    @DeleteMapping("/deleteNew")
    public ResponseEntity<Map<String , Object>> deleteNew(@RequestHeader(value = "id") String deleteNewRequestId , @RequestBody TokenInfo authTokenInfo) {
        response.clear();

        String authToken = authTokenInfo.getId();

        if (authToken.trim().isEmpty()){
            response.put("mensaje", "No autorizado");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        User temp = userRepository.findByAuthToken(authToken);
        if (temp == null || !temp.getUserGroup().equals("MARKETING")) {
            response.put("mensaje", "No autorizado");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if(deleteNewRequestId == null || deleteNewRequestId.isEmpty()) {
            response.put("mensaje" , "El id de la petición no es correcto");
            response.put("status" , HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
        }

        News tempNew = newsRepository.getNewsById(deleteNewRequestId);

        if(tempNew == null) {
            response.put("mensaje" , "Id no valido");
            response.put("status" , HttpStatus.CONFLICT);

            return new ResponseEntity<>(response , HttpStatus.CONFLICT);
        }

        newsRepository.delete(tempNew);

        response.put("mensaje" , "La operación se ha realizado exitosamente.");
        response.put("status" , HttpStatus.OK);

        return ResponseEntity.ok(response);
    }
}
