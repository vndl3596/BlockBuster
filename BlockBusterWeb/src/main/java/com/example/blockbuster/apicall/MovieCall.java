package com.example.blockbuster.apicall;

import com.example.blockbuster.dto.ImageDTO;
import com.example.blockbuster.dto.MovieDTO;
import com.example.blockbuster.dto.MovieRateDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MovieCall {
    private static RestTemplate restTemplate = new RestTemplate();

    public static float getMovieRate(int movie) {
        String uriMVRate = "http://localhost:8080/api/movieDetail/getMovieRate/" + movie;
        ResponseEntity<MovieRateDTO> responseMVRate = restTemplate.getForEntity(uriMVRate, MovieRateDTO.class);
        return responseMVRate.getBody().getRate();
    }

    public static ArrayList<MovieDTO> getAllMovie() {
        ArrayList<MovieDTO> listAllMovie = new ArrayList<>();
        String uriAllMovie = "http://localhost:8080/api/movieDetail/getMovieDetailAll";
        ResponseEntity<MovieDTO[]> responseAllMovie = restTemplate.getForEntity(uriAllMovie, MovieDTO[].class);
        Collections.addAll(listAllMovie, responseAllMovie.getBody());
        for (MovieDTO mv : listAllMovie) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }
        Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        return listAllMovie;
    }

    public static MovieDTO getMovieById(int movie) {
        String uriMovie = "http://localhost:8080/api/movieDetail/getMovieDetail/" + movie;
        ResponseEntity<MovieDTO> responseMovie = restTemplate.getForEntity(uriMovie, MovieDTO.class);
        MovieDTO mv = responseMovie.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("url", mv.getPoster());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        String urlImage = "http://localhost:8080/getImage";
        ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
        mv.setPoster(response.getBody().getUrl());

        return mv;
    }

    public static ArrayList<MovieDTO> getAllMovieForHome() {
        ArrayList<MovieDTO> listAllMovieForHome = MovieCall.getAllMovie();
        Collections.sort(listAllMovieForHome, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        for (int i = listAllMovieForHome.size() - 1; listAllMovieForHome.size() > 16; i--) {
            listAllMovieForHome.remove(i);
        }
        return listAllMovieForHome;
    }

    public static ArrayList<MovieDTO> getListMovieForBigSliderHome() {
        ArrayList<MovieDTO> listMovieForBigSliderHome = new ArrayList<>();
        ArrayList<MovieDTO> listTmpSlider = new ArrayList<>();
        listTmpSlider = MovieCall.getAllMovie();
        Collections.shuffle(listTmpSlider);
        for (MovieDTO mv : listTmpSlider) {
            if (listMovieForBigSliderHome.size() < 6) {
                listMovieForBigSliderHome.add(mv);
            }
        }

        return listMovieForBigSliderHome;
    }

    public static ArrayList<MovieDTO> getListNewestMovie() {
        ArrayList<MovieDTO> listNewestMovie = new ArrayList<>();
        ArrayList<MovieDTO> listTmpNewestMovie = MovieCall.getAllMovie();
        Collections.sort(listTmpNewestMovie, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        for (MovieDTO mv : listTmpNewestMovie) {
            if (listNewestMovie.size() < 10) {
                listNewestMovie.add(mv);
            }
        }

        return listNewestMovie;
    }

    public static ArrayList<MovieDTO> getAllShowingMovie() {
        ArrayList<MovieDTO> listAllShowingMovie = new ArrayList<>();
        String uriAllShowingMovie = "http://localhost:8080/api/movieDetail/getMovieShowinglAll";
        ResponseEntity<MovieDTO[]> responseAllShowingMovie = restTemplate.getForEntity(uriAllShowingMovie, MovieDTO[].class);
        Collections.addAll(listAllShowingMovie, responseAllShowingMovie.getBody());
        for (MovieDTO mv : listAllShowingMovie) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }
        Collections.sort(listAllShowingMovie, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        return listAllShowingMovie;
    }

    public static ArrayList<MovieDTO> getAllComingMovie() {
        ArrayList<MovieDTO> listAllComingMovie = new ArrayList<>();
        String uriAllComingMovie = "http://localhost:8080/api/movieDetail/getMovieCominglAll";
        ResponseEntity<MovieDTO[]> responseAllComingMovie = restTemplate.getForEntity(uriAllComingMovie, MovieDTO[].class);
        Collections.addAll(listAllComingMovie, responseAllComingMovie.getBody());
        for (MovieDTO mv : listAllComingMovie) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }
        Collections.sort(listAllComingMovie, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        return listAllComingMovie;
    }

    public static ArrayList<MovieDTO> getAllMovieOnCast(int actor) {
        ArrayList<MovieDTO> listMovieOnCast = new ArrayList<>();
        String uriMovieOnCast = "http://localhost:8080/api/fkCast/movie/" + actor;
        ResponseEntity<MovieDTO[]> responseMovieOnCast = restTemplate.getForEntity(uriMovieOnCast, MovieDTO[].class);
        Collections.addAll(listMovieOnCast, responseMovieOnCast.getBody());
        for (MovieDTO mv : listMovieOnCast) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }
        Collections.sort(listMovieOnCast, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        return listMovieOnCast;
    }

    public static ArrayList<MovieDTO> getAllMovieOnDirector(int dir) {
        ArrayList<MovieDTO> listMovieOnDir = new ArrayList<>();
        String uriMovieOnDirector = "http://localhost:8080/api/fkDirector/movie/" + dir;
        ResponseEntity<MovieDTO[]> responseMovieOnDirector = restTemplate.getForEntity(uriMovieOnDirector, MovieDTO[].class);
        Collections.addAll(listMovieOnDir, responseMovieOnDirector.getBody());
        for (MovieDTO mv : listMovieOnDir) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }
        Collections.sort(listMovieOnDir, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        return listMovieOnDir;
    }

    public static ArrayList<MovieDTO> getAllMovieOnGenre(int genre) {
        ArrayList<MovieDTO> listMovieOnGenre = new ArrayList<>();
        String uriMovieOnGenre = "http://localhost:8080/api/fkGenre/getAllMovie/" + genre;
        ResponseEntity<MovieDTO[]> responseMovieOnGenre = restTemplate.getForEntity(uriMovieOnGenre, MovieDTO[].class);
        Collections.addAll(listMovieOnGenre, responseMovieOnGenre.getBody());
        for (MovieDTO mv : listMovieOnGenre) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }
        Collections.sort(listMovieOnGenre, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        return listMovieOnGenre;
    }

    public static ArrayList<MovieDTO> getAllComingMovieOnGenre(int genre) {
        ArrayList<MovieDTO> listMovieOnGenre = new ArrayList<>();
        String uriMovieOnGenre = "http://localhost:8080/api/fkGenre/getAllComingMovie/" + genre;
        ResponseEntity<MovieDTO[]> responseMovieOnGenre = restTemplate.getForEntity(uriMovieOnGenre, MovieDTO[].class);
        Collections.addAll(listMovieOnGenre, responseMovieOnGenre.getBody());
        for (MovieDTO mv : listMovieOnGenre) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }
        Collections.sort(listMovieOnGenre, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        return listMovieOnGenre;
    }

    public static ArrayList<MovieDTO> getAllShowingMovieOnGenre(int genre) {
        ArrayList<MovieDTO> listMovieOnGenre = new ArrayList<>();
        String uriMovieOnGenre = "http://localhost:8080/api/fkGenre/getAllShowingMovie/" + genre;
        ResponseEntity<MovieDTO[]> responseMovieOnGenre = restTemplate.getForEntity(uriMovieOnGenre, MovieDTO[].class);
        Collections.addAll(listMovieOnGenre, responseMovieOnGenre.getBody());
        for (MovieDTO mv : listMovieOnGenre) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", mv.getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            mv.setPoster(response.getBody().getUrl());
        }
        Collections.sort(listMovieOnGenre, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                int compare = -o1.getReleaseDate().compareTo(o2.getReleaseDate());
                if (compare == 0) return 1;
                else return compare;
            }
        });
        return listMovieOnGenre;
    }
}
