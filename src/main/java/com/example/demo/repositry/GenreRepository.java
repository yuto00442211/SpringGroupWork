package com.example.demo.repositry;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Genre;

public interface GenreRepository extends CrudRepository<Genre, Integer>{

	@Query("SELECT g FROM genre g WHERE g.genre_name = :text")
	Genre chkGenreName(@Param("text") String text);

	@Query("SELECT g.genre_name FROM Genre g WHERE g.genre_id = :id")
	String getGenreName(@Param("id") int id);
}
