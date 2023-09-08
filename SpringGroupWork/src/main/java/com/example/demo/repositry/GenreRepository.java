package com.example.demo.repositry;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Genre;

public interface GenreRepository extends CrudRepository<Genre, Integer>{

}
