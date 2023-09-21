package com.example.demo.service.IMPL;

import com.example.demo.dto.FKGenreDTO;
import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.dto.MovieGenreDTO;
import com.example.demo.map.FKGenreMap;
import com.example.demo.map.MovieDetailMap;
import com.example.demo.map.MovieGenreMap;
import com.example.demo.model.FKGenre;
import com.example.demo.model.Key.FkGenreKey;
import com.example.demo.model.MovieDetail;
import com.example.demo.repository.FKGenreRepository;
import com.example.demo.repository.MovieDetailRepository;
import com.example.demo.repository.MovieGenreRepository;
import com.example.demo.service.FKGenreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FKGenreServiceImpl implements FKGenreService {
    private final FKGenreRepository fkGenreRepository;
    private final MovieDetailRepository movieDetailRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieDetailMap movieDetailMap;
    private final MovieGenreMap movieGenreMap;
    private final FKGenreMap fkGenreMap;

    @Override
    public List<FKGenreDTO> getAllFKGenre() {
        return fkGenreMap.listGenreToListDTO(fkGenreRepository.findAll());
    }

    @Override
    public List<MovieGenreDTO> getGenreOnMovie(int movieId) {
        List<MovieGenreDTO> movieGenreDTOS = new ArrayList<>();
        List<FKGenre> fkGenres = fkGenreRepository.findAll();
        fkGenres.forEach(fkGenre -> {
            if (fkGenre.getMovieDetail().getId() == movieId) {
                movieGenreDTOS.add(movieGenreMap.movieGenreToDTO(fkGenre.getMovieGenre()));
            }
        });
        return movieGenreDTOS;
    }

    @Override
    public List<MovieDetailDTO> getMovieOnGenre(int genreId) {
        List<MovieDetailDTO> movieDetailDTOS = new ArrayList<>();
        List<FKGenre> fkGenres = fkGenreRepository.findAll();
        fkGenres.forEach(fkGenre -> {
            if (fkGenre.getMovieGenre().getId() == genreId) {
                movieDetailDTOS.add(movieDetailMap.movieDetailToDTO(fkGenre.getMovieDetail()));
            }
        });
        return movieDetailDTOS;
    }

    @Override
    public List<MovieDetailDTO> getComingMovieOnGenre(int genreId) {
        List<MovieDetailDTO> movieDetailDTOS = new ArrayList<>();
        List<FKGenre> fkGenres = fkGenreRepository.findAll();
        fkGenres.forEach(fkGenre -> {
            if ((fkGenre.getMovieGenre().getId() == genreId) && (fkGenre.getMovieDetail().getMovieStatus() == false)) {
                movieDetailDTOS.add(movieDetailMap.movieDetailToDTO(fkGenre.getMovieDetail()));
            }
        });
        return movieDetailDTOS;
    }

    @Override
    public List<MovieDetailDTO> getShowingMovieOnGenre(int genreId) {
        List<MovieDetailDTO> movieDetailDTOS = new ArrayList<>();
        List<FKGenre> fkGenres = fkGenreRepository.findAll();
        fkGenres.forEach(fkGenre -> {
            if ((fkGenre.getMovieGenre().getId() == genreId) && (fkGenre.getMovieDetail().getMovieStatus() == true)) {
                movieDetailDTOS.add(movieDetailMap.movieDetailToDTO(fkGenre.getMovieDetail()));
            }
        });
        return movieDetailDTOS;
    }

    @Override
    public String addGenreToMovie(String[] genres, int movieId) {
        FKGenre fkGenre = new FKGenre();
        FkGenreKey fkGenreKey = new FkGenreKey();
        fkGenreKey.setMovieId(movieId);

        for (FKGenre fk: fkGenreRepository.findAll()) {
            if(fk.getMovieDetail().getId() == movieId){
                fkGenreRepository.delete(fk);
            }
        }

        for (String genre: genres) {
            if(!genre.equals("0")){
                fkGenreKey.setGenreId(Integer.parseInt(genre));
                fkGenre.setId(fkGenreKey);
                fkGenre.setMovieGenre(movieGenreRepository.getById(Integer.parseInt(genre)));
                fkGenre.setMovieDetail(movieDetailRepository.getById(movieId));
                fkGenreRepository.save(fkGenre);
            }
        }
        return "Success";
    }

    @Override
    public void deleteByGenreId(int genreId) {
        List<FKGenre> fkGenres = fkGenreRepository.findAll();
        fkGenres.forEach(fkGenre -> {
            if (fkGenre.getMovieGenre().getId() == genreId) {
                fkGenreRepository.delete(fkGenre);
            }
        });
    }

    @Override
    public void deleteByMovieId(int movieId) {
        List<FKGenre> fkGenres = fkGenreRepository.findAll();
        fkGenres.forEach(fkGenre -> {
            if (fkGenre.getMovieDetail().getId() == movieId) {
                fkGenreRepository.delete(fkGenre);
            }
        });
    }

    @Override
    public void removeGenreExits(Integer movieId) {
        List<FKGenre> fkGenresPresent = fkGenreRepository.findAll();
            for (FKGenre genre : fkGenresPresent) {
                if (genre.getId().getMovieId() == movieId) {
                    fkGenreRepository.delete(genre);
                }
            }
        }
    }
