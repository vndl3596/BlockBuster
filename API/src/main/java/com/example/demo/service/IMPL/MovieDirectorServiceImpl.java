package com.example.demo.service.IMPL;

import com.example.demo.DTO.DirectorPage;
import com.example.demo.DTO.MovieDirectorDTO;
import com.example.demo.map.MovieDirectorMap;
import com.example.demo.model.MovieDirector;
import com.example.demo.repository.MovieDirectorRepository;
import com.example.demo.service.FKDirectorService;
import com.example.demo.service.MovieDirectorService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieDirectorServiceImpl implements MovieDirectorService {
    private final MovieDirectorRepository movieDirectorRepository;
    private final MovieDirectorMap movieDirectorMap;
    private final FKDirectorService fkDirectorService;

    @Override
    public List<MovieDirectorDTO> getAllMovieDirector() {
        return movieDirectorMap.listMovieDirectorToDTO(movieDirectorRepository.findAll());
    }

    @Override
    public MovieDirectorDTO getMovieDirectorById(int id) {
        return movieDirectorMap.movieDirectorToDTO(movieDirectorRepository.getById(id));
    }

    @Override
    public String deleteMovieDirectorById(int id) {
        MovieDirector movieDirector = movieDirectorRepository.findById(id).orElse(null);
        if (movieDirector == null) {
            throw new RuntimeException("Director not found");
        } else {
            fkDirectorService.deleteFkDirectorByDirectorId(id);
            movieDirectorRepository.delete(movieDirector);
            return "Delete a director successfully";
        }
    }

    @Override
    public MovieDirectorDTO createMovieDirector(MovieDirectorDTO movieDirectorDTO) {
        System.out.println(movieDirectorDTO.getBirthday());
        if (checkNameInDirector(movieDirectorDTO.getName()) == false) {
            MovieDirector movieDirector = new MovieDirector();
            movieDirector.setAvatar(movieDirectorDTO.getAvatar());
            movieDirector.setName(movieDirectorDTO.getName());
            movieDirector.setStory(movieDirectorDTO.getStory());
            movieDirector.setBirthday(movieDirectorDTO.getBirthday());
            movieDirectorRepository.save(movieDirector);
            return movieDirectorDTO;
        }
        return null;
    }

    @Override
    public String editMovieDirector(MovieDirectorDTO movieDirectorDTO) {
        MovieDirector movieDirector = movieDirectorRepository.findById(movieDirectorDTO.getId()).orElse(null);
        if (movieDirector == null) {
            throw new RuntimeException("Director not found");
        } else {
            if (checkNameInDirector(movieDirectorDTO.getName()) == false) {
                movieDirector.setAvatar(movieDirectorDTO.getAvatar());
                movieDirector.setName(movieDirectorDTO.getName());
                movieDirector.setStory(movieDirector.getStory());
                movieDirectorRepository.save(movieDirector);
                return "Edit director successfully";
            }
            return "Fail";
        }
    }

    @Override
    public DirectorPage getAllDirectorPage(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<MovieDirector> directors = movieDirectorRepository.findAll(PageRequest.of(pageNo, pageSize, sort));
        // get content for page object
        List<MovieDirector> listOfPosts = directors.getContent();

        List<MovieDirectorDTO> content = movieDirectorMap.listMovieDirectorToDTO(listOfPosts);
        DirectorPage directorPage = new DirectorPage();
        directorPage.setMovieDirectorDTOS(content);
        directorPage.setPageNo(directors.getNumber());
        directorPage.setPageSize(directors.getSize());
        directorPage.setTotalElements(directors.getTotalElements());
        directorPage.setTotalPages(directors.getTotalPages());
        directorPage.setFirst(directors.isFirst());
        directorPage.setLast(directors.isLast());

        return directorPage;
    }

    public boolean checkNameInDirector(String name) {
        List<MovieDirector> movieDirectors = movieDirectorRepository.findAll();
        movieDirectors.forEach(movieDirector -> {
            if (movieDirector.getName().equals(name)) {
                throw new RuntimeException("Director's name already exits");
            }
        });
        return false;
    }
}
