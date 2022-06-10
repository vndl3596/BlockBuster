package com.example.demo.service.IMPL;

import com.example.demo.DTO.FKGenreDTO;
import com.example.demo.DTO.MovieDetailDTO;
import com.example.demo.DTO.MovieGenreDTO;
import com.example.demo.map.FKGenreMap;
import com.example.demo.map.MovieDetailMap;
import com.example.demo.map.MovieGenreMap;
import com.example.demo.model.FKGenre;
import com.example.demo.model.Key.FkGenreKey;
import com.example.demo.repository.FKGenreRepository;
import com.example.demo.service.FKGenreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FKGenreServiceImpl implements FKGenreService {
    private final FKGenreRepository fkGenreRepository;
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
    public FKGenreDTO addGenreToMovie(FKGenreDTO fkGenreDTO) {
        FKGenre fkGenre = new FKGenre();
        FkGenreKey fkGenreKey = new FkGenreKey();
        fkGenreKey.setGenreId(fkGenreDTO.getMovieGenreId());
        fkGenreKey.setMovieId(fkGenreDTO.getMovieDetailId());

        fkGenre.setId(fkGenreKey);
//        fkGenre.setMovieDetail(movieDetailMap.DTOToMovieDetail(movieDetailService.getMovieById(fkGenreDTO.getMovieDetailId())));
//        fkGenre.setMovieGenre(movieGenreMap.DTOToMovieGenre(movieGenreService.getMovieGenreById(fkGenreDTO.getMovieGenreId())));

        fkGenreRepository.save(fkGenre);
        return fkGenreDTO;

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
}
