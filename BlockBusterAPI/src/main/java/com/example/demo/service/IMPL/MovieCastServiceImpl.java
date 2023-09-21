package com.example.demo.service.IMPL;

import com.example.demo.dto.CastPage;
import com.example.demo.dto.MovieCastDTO;
import com.example.demo.map.MovieCastMap;
import com.example.demo.map.MovieDetailMap;
import com.example.demo.model.MovieCast;
import com.example.demo.repository.FKCastRepository;
import com.example.demo.repository.MovieCastRepository;
import com.example.demo.service.FKCastService;
import com.example.demo.service.MovieCastService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@AllArgsConstructor
public class MovieCastServiceImpl implements MovieCastService {
    private final MovieCastRepository movieCastRepository;
    private final MovieCastMap movieCastMap;
    private final FKCastService fkCastService;
    private final FKCastRepository fkCastRepository;
    private final MovieDetailMap movieDetailMap;

    @Override
    public List<MovieCastDTO> getAllCast() {
        return movieCastMap.listMovieCastToDTO(movieCastRepository.findAll());
    }

    @Override
    public MovieCastDTO getMovieCastById(int id) {
        return movieCastMap.movieCastDTO(movieCastRepository.getById(id));
    }


    @Override
    public String deleteMovieCastById(int id) {
        MovieCast movieCast = movieCastRepository.findById(id).orElse(null);
        if (movieCast == null) {
            throw new RuntimeException("Cast not found");
        } else {
            fkCastService.deleteFkCastByCastId(id);
            movieCastRepository.delete(movieCast);
            File avt = new File(movieCast.getAvatar());
            avt.delete();
            return "Delete cast successfully";
        }
    }

    @Override
    public MovieCastDTO createMovieCast(MovieCastDTO movieCastDTO) {
            MovieCast movieCast = new MovieCast();
            movieCast.setAvatar(movieCastDTO.getAvatar());
            movieCast.setName(movieCastDTO.getName());
            movieCast.setStory(movieCastDTO.getStory());
            movieCast.setBirthday(movieCastDTO.getBirthday());
            movieCastRepository.save(movieCast);
            movieCast.setAvatar("./image/cast/act-" + movieCast.getId() + movieCastDTO.getAvatar());
            movieCastDTO.setId(movieCast.getId());
            movieCastDTO.setAvatar(movieCast.getAvatar());
            movieCastRepository.save(movieCast);
            return movieCastDTO;
    }

    @Override
    public MovieCastDTO editMovieCast(MovieCastDTO movieCastDTO) {
        MovieCast movieCast = movieCastRepository.findById(movieCastDTO.getId()).orElse(null);
        if (movieCast == null) {
            throw new RuntimeException("Cast not found");
        } else {
            File avt = new File(movieCast.getAvatar());
            if(!movieCastDTO.getAvatar().equals("false")){
                movieCast.setAvatar("./image/cast/act-" + movieCast.getId() + movieCastDTO.getAvatar());
            }
            movieCastDTO.setAvatar(movieCast.getAvatar());
            movieCast.setName(movieCastDTO.getName());
            movieCast.setBirthday(movieCastDTO.getBirthday());
            movieCast.setStory(movieCast.getStory());
            movieCastRepository.save(movieCast);
            if(!movieCastDTO.getAvatar().equals("false")){
                avt.delete();
            }
            return movieCastDTO;
        }
    }

    @Override
    public CastPage getAllCastPage(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // create Pageable instance
//        Pageable pageable = ;
        Page<MovieCast> casts = movieCastRepository.findAll(PageRequest.of(pageNo, pageSize, sort));
        // get content for page object
        List<MovieCast> listOfPosts = casts.getContent();

        List<MovieCastDTO> content = movieCastMap.listMovieCastToDTO(listOfPosts);
        CastPage castPage = new CastPage();
        castPage.setMovieCastDTOS(content);
        castPage.setPageNo(casts.getNumber());
        castPage.setPageSize(casts.getSize());
        castPage.setTotalElements(casts.getTotalElements());
        castPage.setTotalPages(casts.getTotalPages());
        castPage.setFirst(casts.isFirst());
        castPage.setLast(casts.isLast());

        return castPage;
    }


    public boolean checkNameExit(String name) {
        List<MovieCast> movieCasts = movieCastRepository.findAll();
        movieCasts.forEach(movieCast -> {
            if (movieCast.getName().equals(name)) {
                throw new RuntimeException("Cast's name already exists");
            }
        });
        return false;
    }
}
