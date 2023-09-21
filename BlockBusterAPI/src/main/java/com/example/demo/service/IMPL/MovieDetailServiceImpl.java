package com.example.demo.service.IMPL;

import com.example.demo.dto.*;
import com.example.demo.map.MovieCastMap;
import com.example.demo.map.MovieDetailMap;
import com.example.demo.map.MovieEvaluateMap;
import com.example.demo.map.MovieGenreMap;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieDetailServiceImpl implements MovieDetailService {
    private final MovieDetailRepository movieDetailRepository;
    private final FKGenreService fkGenreService;
    private final FKDirectorService fkDirectorService;
    private final FKCastService fkCastService;
    private final MovieEvaluateService movieEvaluateService;
    private final MovieDetailMap movieDetailMap;
    private final MovieGenreMap movieGenreMap;
    private final MovieCastMap movieCastMap;
    private final MovieEvaluateMap movieEvaluateMap;
    private final MembershipRepository membershipRepository;
    private final MovieCastRepository movieCastRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieDirectorRepository movieDirectorRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<MovieDetailDTO> getAllMovie() {
        return movieDetailMap.listMovieDetailToDTO(movieDetailRepository.findAll());
    }

    @Override
    public List<MovieDetailDTO> getAllShowingMovie() {
        List<MovieDetailDTO> showingList = new ArrayList<>();
        for (MovieDetailDTO mv: movieDetailMap.listMovieDetailToDTO(movieDetailRepository.findAll())) {
            if(mv.getMovieStatus() == true){
                showingList.add(mv);
            }
        }
        return showingList;
    }

    @Override
    public List<MovieDetailDTO> getAllCommingMovie() {
        List<MovieDetailDTO> showingList = new ArrayList<>();
        for (MovieDetailDTO mv: movieDetailMap.listMovieDetailToDTO(movieDetailRepository.findAll())) {
            if(mv.getMovieStatus() == false){
                showingList.add(mv);
            }
        }
        return showingList;
    }

    @Override
    public MovieDetailDTO getMovieById(int movieId) {
        return movieDetailMap.movieDetailToDTO(movieDetailRepository.getById(movieId));
    }

    @Override
    public MovieDetailPage getAllMovieDetailPage(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // create Pageable instance
//        Pageable pageable = ;
        Page<MovieDetail> movieDetails = movieDetailRepository.findAll(PageRequest.of(pageNo, pageSize, sort));
        // get content for page object
        List<MovieDetail> listOfPosts = movieDetails.getContent();

        List<MovieDetailDTO> content = movieDetailMap.listMovieDetailToDTO(listOfPosts);
        MovieDetailPage MovieDetailPage = new MovieDetailPage();
        MovieDetailPage.setMovieDetailDTOS(content);
        MovieDetailPage.setPageNo(movieDetails.getNumber());
        MovieDetailPage.setPageSize(movieDetails.getSize());
        MovieDetailPage.setTotalElements(movieDetails.getTotalElements());
        MovieDetailPage.setTotalPages(movieDetails.getTotalPages());
        MovieDetailPage.setFirst(movieDetails.isFirst());
        MovieDetailPage.setLast(movieDetails.isLast());

        return MovieDetailPage;
    }

    @Override
    public MovieDetailDTO addMovieDetail(MovieDetailDTO movieDetailDTO) throws Exception {
        MovieDetail mv = new MovieDetail();
        mv.setTitle(movieDetailDTO.getTitle());
        mv.setDetail(movieDetailDTO.getDetail());
        mv.setLinkTrailer(movieDetailDTO.getLinkTrailer());
        mv.setLinkMovie(movieDetailDTO.getLinkMovie());
        mv.setMovieDuration(movieDetailDTO.getMovieDuration());
        mv.setViewNumber(0);
        mv.setMovieStatus(movieDetailDTO.getMovieStatus());
        mv.setRequireMember(membershipRepository.getById(movieDetailDTO.getRequireMember().getId()));
        mv.setReleaseDate(movieDetailDTO.getReleaseDate());
        mv.setPoster(movieDetailDTO.getPoster());
        movieDetailRepository.save(mv);
        mv.setPoster("./image/movie/mv-" + mv.getId() + movieDetailDTO.getPoster());
        movieDetailRepository.save(mv);
        movieDetailDTO.setPoster(mv.getPoster());
        movieDetailDTO.setId(mv.getId());
        return movieDetailDTO;
    }

    private boolean checkExitTitleAddMovie(String title) {
        List<MovieDetail> movieDetails = movieDetailRepository.findAll();
        for (MovieDetail movieDetail : movieDetails) {
            if (movieDetail.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int addView(int mvId){
        MovieDetail mv =  movieDetailRepository.getById(mvId);
        mv.setViewNumber(mv.getViewNumber() + 1);
        movieDetailRepository.save(mv);
        return mv.getViewNumber();
    }
    @Override
    public MovieDetailDTO editMovieDetail(MovieDetailDTO movieDetailDTO) throws Exception {
        MovieDetail mv = movieDetailRepository.getById(movieDetailDTO.getId());
        if (mv == null) {
            throw new RuntimeException("Movie not found");
        } else {
            File pos = new File(movieDetailDTO.getPoster());
            if(!movieDetailDTO.getPoster().equals("false")){
                mv.setPoster("./image/movie/mv-" + mv.getId() + movieDetailDTO.getPoster());
            }
            movieDetailDTO.setPoster(mv.getPoster());
            mv.setTitle(movieDetailDTO.getTitle());
            mv.setMovieStatus(movieDetailDTO.getMovieStatus());
            mv.setRequireMember(membershipRepository.getById(movieDetailDTO.getRequireMember().getId()));
            mv.setMovieDuration(movieDetailDTO.getMovieDuration());
            mv.setDetail(movieDetailDTO.getDetail());
            mv.setLinkMovie(movieDetailDTO.getLinkMovie());
            mv.setLinkTrailer(movieDetailDTO.getLinkTrailer());
            mv.setReleaseDate(movieDetailDTO.getReleaseDate());
            movieDetailRepository.save(mv);
            if(!movieDetailDTO.getPoster().equals("false")){
                pos.delete();
            }
            return movieDetailDTO;
        }
    }

    @Override
    public String deleteMovieDetail(int id) throws Exception {
        MovieDetail movieDetail = movieDetailRepository.findById(id).orElse(null);
        if (movieDetail == null) {
            throw new Exception("Move not found");
        } else {
            fkCastService.deleteFkCastByMovieId(id);
            fkDirectorService.deleteFkDirectorByMovieId(id);
            fkGenreService.deleteByMovieId(id);
            movieEvaluateService.deleteMovieEvaluateByMovieId(id);
            movieDetailRepository.delete(movieDetail);
            return "Success";
        }
    }

    @Override
    public MovieDetail getMovieDetailByTitle(String title) {
        return movieDetailRepository.findMovieDetailByTitle(title);
    }

    @Override
    public List<MovieRate> getListMovieRate() throws Exception {
        List<MovieRate> movieRates = new ArrayList<>();
        List<MovieDetail> movieDetails = movieDetailRepository.findAll();
        for (MovieDetail movieDetail : movieDetails) {
            if (movieDetail.getMovieEvaluates().isEmpty()) {
                movieRates.add(new MovieRate(movieDetail.getId(), 0.0));
            } else {
                movieRates.add(getRateMovie(movieDetail.getId()));
            }
        }
        return movieRates.stream().sorted(Comparator.comparing(MovieRate::getRate).reversed()).collect(Collectors.toList());
    }

    @Override
    public MovieRate getRateMovie(int id) throws Exception {
        MovieDetail movieDetail = movieDetailRepository.findById(id).orElse(null);
        if (movieDetail != null) {
            List<MovieEvaluate> movieEvaluates = movieEvaluateService.getMovieEvaluates();
            int sumRate = 0;
            int countMovie = 0;
            assert movieEvaluates != null;
            for (MovieEvaluate movieEvaluate : movieEvaluates) {
                if (movieEvaluate.getId().getMovieId() == id) {
                    sumRate += movieEvaluate.getEvaluateRate();
                    countMovie = countMovie + 1;
                }
            }
            if (countMovie != 0) {
                return new MovieRate(movieDetail.getId(), ((double) sumRate / (double) countMovie));
            } else {
                return new MovieRate(movieDetail.getId(), 0.0);
            }
        } else {
            throw new Exception("Movie not found!");
        }
    }

    @Override
    public List<MovieGenreDTO> getMovieGenres(int id) {
        MovieDetail movieDetail = movieDetailRepository.getById(id);
        List<FKGenre> fkGenres = new ArrayList<>();
        List<MovieGenre> genreList = new ArrayList<>();
        if (movieDetail.getFkGenres() != null || movieDetail.getFkGenres().size() != 0) {
            fkGenres = movieDetail.getFkGenres();
        }
        for (FKGenre fkGenre : fkGenres) {
            genreList.add(fkGenre.getMovieGenre());
        }
        return movieGenreMap.listMovieGenreToDTO(genreList);
    }

    @Override
    public List<MovieCastDTO> getMovieCasts(int id) {
        MovieDetail movieDetailCast = movieDetailRepository.getById(id);
        List<FKCast> fkCasts = new ArrayList<>();
        List<MovieCast> castList = new ArrayList<>();
        if (movieDetailCast.getFkCasts() != null || movieDetailCast.getFkCasts().size() != 0) {
            fkCasts = movieDetailCast.getFkCasts();
        }
        for (FKCast fkCast : fkCasts) {
            castList.add(fkCast.getMovieCast());
        }
        return movieCastMap.listMovieCastToDTO(castList);
    }

    @Override
    public List<MovieDetailDTO> search(String searchQuery) {
        return movieDetailMap.listMovieDetailToDTO(movieDetailRepository.findByTitleLike(searchQuery));
    }

    @Override
    public List<MovieEvaluateDTO> loadEvaluate(int movieId) {
        List<MovieEvaluate> movieEvaluates = movieEvaluateService.getMovieEvaluates();
        List<MovieEvaluateDTO> movieEvaluateDTOS = new ArrayList<>();
        for (MovieEvaluate movieEvaluate : movieEvaluates) {
            if (movieEvaluate.getId().getMovieId() == movieId) {
                movieEvaluateDTOS.add(movieEvaluateMap.movieEvaluateToDTO(movieEvaluate));
            }
        }
        return movieEvaluateDTOS;
    }

    @Override
    public List<MovieEvaluateDTO> loadEvaluateInAcc(int accId) {
        List<MovieEvaluate> movieEvaluates = movieEvaluateService.getMovieEvaluates();
        List<MovieEvaluateDTO> movieEvaluateDTOS = new ArrayList<>();
        for (MovieEvaluate movieEvaluate : movieEvaluates) {
            if (movieEvaluate.getId().getUserId() == accId) {
                movieEvaluateDTOS.add(movieEvaluateMap.movieEvaluateToDTO(movieEvaluate));
            }
        }
        return movieEvaluateDTOS;
    }

    @Override
    public MovieEvaluateDTO saveEvaluate(MovieEvaluateDTO movieEvaluateDTO) {
        MovieDetail movieDetail = movieDetailRepository.getById(movieEvaluateDTO.getMovieId().getId());
        MovieEvaluate movieEvaluate = movieEvaluateMap.dTOToMovieEvaluate(movieEvaluateDTO);
        List<MovieEvaluate> movieEvaluates = new ArrayList<>();
        if (movieDetail.getMovieEvaluates() != null) {
            movieEvaluates = movieDetail.getMovieEvaluates();
            for (MovieEvaluate movieEvaluateCheck : movieEvaluates) {
                if ((movieEvaluateCheck.getId().getUserId() == movieEvaluate.getId().getUserId()) &&
                        (movieEvaluateCheck.getId().getMovieId() == movieEvaluate.getId().getMovieId())) {
                    movieEvaluateService.editEvaluate(movieEvaluate);
                    return movieEvaluateDTO;
                }
            }
            movieEvaluates.add(movieEvaluate);
        } else {
            movieEvaluates.add(movieEvaluate);
        }
        movieDetail.setMovieEvaluates(movieEvaluates);
        movieDetailRepository.save(movieDetail);
        return movieEvaluateDTO;
    }

    public boolean checkExitTitleEditMovie(String title, int id) {
        List<MovieDetail> movieDetails = movieDetailRepository.findAll();
        for (MovieDetail movieDetail : movieDetails) {
            if (movieDetail.getTitle().equals(title) && (id != movieDetail.getId())) {
                return true;
            }
        }
        return false;
    }
}