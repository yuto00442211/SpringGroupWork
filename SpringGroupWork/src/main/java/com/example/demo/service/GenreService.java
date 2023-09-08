package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Genre;
import com.example.demo.repositry.GenreRepository;

@Service
public class GenreService {

	private GenreRepository genreRepository;
	
	public GenreService(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}
	
	public List<Genre> getAllGenre(){
		List<Genre> genreList = (List<Genre>) genreRepository.findAll();
		return genreList;
	}
	
}
