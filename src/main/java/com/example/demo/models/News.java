package com.example.demo.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Document( collection = "News")
@TypeAlias("news")
public class News {
    // Para poder formatear la fecha , en caso de que la manden nula
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");


    @Id
    private String id;

    private String title;
    private String date;
    private String author;
    private String category;
    private String image;
    private String description;
    private String content;
    private String link;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String buttonText;


    /**
     * Este metodo permite determinar si el formato de la noticia es valido y en caso de que lo sea
     * // corregir el de aquellos que no sean obligatorios
     * @return
     */
    public boolean isNotValid() {
        // CAMPOS OBLIGATORIOS

        if (title == null || title.trim().isEmpty()) {
            return true;
        }
        if (author == null || author.trim().isEmpty()) {
            return true;
        }
        if (description == null || description.trim().isEmpty()) {
            return true;
        }
        if (content == null || content.trim().isEmpty()) {
            return true;
        }

        // CAMPOS NO OBLIGATORIOS

        if( date == null || date.isEmpty()) {
            // Entonces ponemos la fecha actual
            date = LocalDateTime.now().format(FORMATTER);
        }

        if(image == null) {
            image = "";
        }

        if(buttonText == null || buttonText.isEmpty()) {
            buttonText = "Botón por defecto";
        }

        if(link == null || link.isEmpty()) {
            link = "Enlace sin definir";
        }

        return false;
    }
}
