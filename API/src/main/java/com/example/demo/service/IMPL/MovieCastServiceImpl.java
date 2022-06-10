package com.example.demo.service.IMPL;

import com.example.demo.DTO.CastPage;
import com.example.demo.DTO.MovieCastDTO;
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
            return "Delete cast successfully";
        }
    }

    @Override
    public MovieCastDTO createMovieCast(MovieCastDTO movieCastDTO) {
        if (checkNameExit(movieCastDTO.getName()) == false) {
            MovieCast movieCast = new MovieCast();
            movieCast.setAvatar(movieCastDTO.getAvatar());
            movieCast.setName(movieCastDTO.getName());
            movieCast.setStory(movieCastDTO.getStory());
            movieCast.setBirthday(movieCastDTO.getBirthday());
            movieCastRepository.save(movieCast);
            return movieCastDTO;
        }
        return null;

    }

    @Override
    public String editMovieCast(MovieCastDTO movieCastDTO) {
        MovieCast movieCast = movieCastRepository.findById(movieCastDTO.getId()).orElse(null);
        if (movieCast == null) {
            throw new RuntimeException("Cast not found");
        } else {
            if (checkNameExit(movieCastDTO.getName())) {
                movieCast.setAvatar(movieCastDTO.getAvatar());
                movieCast.setName(movieCastDTO.getName());
                movieCast.setStory(movieCastDTO.getStory());
                movieCastRepository.save(movieCast);
                return "Edit cast successfully";
            }
            return "Fail";
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
