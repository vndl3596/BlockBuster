package com.example.blockbuster.controller.admin;

import com.example.blockbuster.apicall.DataCall;
import com.example.blockbuster.dto.*;
import org.json.simple.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/admin/")
public class AdminMovieController {
    ArrayList<MovieDTO> listAllMovie;
    ArrayList<GenreDTO> listGenre;
    AccountDTO loginAcc = new AccountDTO();
    LoginResponse loginResponse;

    @RequestMapping("movie")
    public ModelAndView adminMovieShow(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/movie");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("listAllMovie", listAllMovie);
        return new ModelAndView("admin/movie");
    }

    @RequestMapping("movie/view/idMov={id}")
    public ModelAndView adminMovieView(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/movie/view/idMov=" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        MovieDTO viewMV = new MovieDTO();
        for (MovieDTO mv : listAllMovie) {
            if (mv.getId() == id) {
                viewMV = mv;
            }
        }

        String urlAllGenreOnMovie = "http://localhost:8080/api/fkGenre/getAllGenre/" + id;
        ArrayList<GenreDTO> listAllGenreOnMovie = new ArrayList<>();
        ResponseEntity<GenreDTO[]> responseAllGenreOnMovie = restTemplate.getForEntity(urlAllGenreOnMovie, GenreDTO[].class);
        Collections.addAll(listAllGenreOnMovie, responseAllGenreOnMovie.getBody());

        Map<GenreDTO, Integer> mapAllGenreOnMovie = new TreeMap<>(new Comparator<GenreDTO>() {
            @Override
            public int compare(GenreDTO o1, GenreDTO o2) {
                if (o1.getId() >= o2.getId()) {
                    return 1;
                } else return -1;
            }
        });
        boolean loop = false;
        for (GenreDTO genre : listGenre) {
            for (GenreDTO fk : listAllGenreOnMovie) {
                if (genre.getId() == fk.getId()) {
                    loop = true;
                }
            }
            if (loop == true) {
                mapAllGenreOnMovie.put(genre, 1);
            }
            loop = false;
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("mapAllGenreOnMovie", mapAllGenreOnMovie);
        model.addAttribute("viewMV", viewMV);
        model.addAttribute("format", format);
        model.addAttribute("inputDateFormat", inputDateFormat);
        model.addAttribute("listAllMovie", listAllMovie);
        return new ModelAndView("admin/viewmovie");
    }

    @RequestMapping("movie/add")
    public ModelAndView adminMovieAddGet(Model model, HttpSession session, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String error = "";

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/movie/add");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        RestTemplate restTemplate = new RestTemplate();
        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("error", error);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("format", format);
        model.addAttribute("inputDateFormat", inputDateFormat);
        model.addAttribute("listAllMembership", listAllMembership);
        model.addAttribute("listAllMovie", listAllMovie);
        return new ModelAndView("admin/addmovie");
    }

    @RequestMapping(method = RequestMethod.POST, path = "movie/add")
    public ModelAndView adminMovieAddPost(Model model, HttpSession session,
                                          @RequestParam("title") String title,
                                          @RequestParam("requiremember") String requiremember,
                                          @RequestParam("release") String release,
                                          @RequestParam("poster") MultipartFile poster,
                                          @RequestParam("hour") String hour,
                                          @RequestParam("min") String min,
                                          @RequestParam("second") String second,
                                          @RequestParam("status") String status,
                                          @RequestParam("detail") String detail,
                                          @RequestParam("trailer") String trailer,
                                          @RequestParam("movie") String movie,
                                          @RequestParam("genres") String[] genres,
                                          HttpServletResponse servletResponse) throws ParseException, IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fs = poster.getOriginalFilename();
        String error = "";
        String filenameL = fs.substring(fs.indexOf("."), fs.length());
        RestTemplate restTemplate = new RestTemplate();
        boolean stt = false;
        if (status.equals("1")) {
            stt = true;
        }

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/movie/add");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        MembershipDTO member = new MembershipDTO();
        for (MembershipDTO mem : listAllMembership) {
            if (mem.getId() == Integer.parseInt(requiremember)) {
                member = mem;
                break;
            }
        }

        MovieDTO mv = new MovieDTO(0, title, filenameL, detail, stt, trailer, movie, inputDateFormat.parse(release), member, hour + ":" + min + ":" + second, 0);
        String urlAddMv = "http://localhost:8080/api/movieDetail/addMovie";
        HttpEntity<MovieDTO> requestAddMv = new HttpEntity<>(mv);
        ResponseEntity<MovieDTO> responseAddMv = restTemplate.postForEntity(urlAddMv, requestAddMv, MovieDTO.class);
        listAllMovie.add(0, responseAddMv.getBody());

        HttpHeaders headersUploadImage = new HttpHeaders();
        headersUploadImage.setContentType(MediaType.MULTIPART_FORM_DATA);
        Resource muti = poster.getResource();
        MultiValueMap<String, Object> mapUp = new LinkedMultiValueMap<String, Object>();
        mapUp.add("image", muti);
        HttpEntity<MultiValueMap<String, Object>> requestUp = new HttpEntity<>(mapUp, headersUploadImage);
        String urlUpdateImage = "http://localhost:8080/uploadimage/" + "mv-" + responseAddMv.getBody().getId() + "/221";
        restTemplate.postForEntity(urlUpdateImage, requestUp, JSONObject.class);

        HttpHeaders headersGetImage = new HttpHeaders();
        headersGetImage.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("url", listAllMovie.get(0).getPoster());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersGetImage);
        String urlImage = "http://localhost:8080/getImage";
        ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
        listAllMovie.get(0).setPoster(response.getBody().getUrl());

        String urlAddFkGenre = "http://localhost:8080/api/fkGenre/addGenreForMovie/" + responseAddMv.getBody().getId();
        HttpEntity<String[]> requestAddFkGenre = new HttpEntity<>(genres);
        restTemplate.postForEntity(urlAddFkGenre, requestAddFkGenre, String.class);

        error += "Thêm phim thành công!!!";

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("error", error);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("format", format);
        model.addAttribute("inputDateFormat", inputDateFormat);
        model.addAttribute("listAllMembership", listAllMembership);
        model.addAttribute("listAllMovie", listAllMovie);
        return new ModelAndView("admin/addmovie");
    }

    @RequestMapping("movie/delete/idMov={id}")
    public ModelAndView adminMovieDelete(Model model, HttpSession session, @PathVariable(value = "id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String error = "";

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/movie");
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        RestTemplate restTemplate = new RestTemplate();
        String urlDeleteMovie = "http://localhost:8080/api/movieDetail/remove/" + id;
        restTemplate.getForEntity(urlDeleteMovie, String.class);

        for (MovieDTO mv : listAllMovie) {
            if (mv.getId() == id) {
                listAllMovie.remove(mv);
                break;
            }
        }

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("format", format);
        model.addAttribute("listAllMovie", listAllMovie);
        return new ModelAndView("admin/movie");
    }

    @RequestMapping("movie/edit/idMov={id}")
    public ModelAndView adminMovieEditGet(Model model, HttpSession session, @PathVariable("id") int id, HttpServletResponse servletResponse) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();
        String error = "";

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/movie/edit/idMov" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        MovieDTO editMV = new MovieDTO();
        for (MovieDTO mv : listAllMovie) {
            if (mv.getId() == id) {
                editMV = mv;
            }
        }

        String urlAllGenreOnMovie = "http://localhost:8080/api/fkGenre/getAllGenre/" + id;
        ArrayList<GenreDTO> listAllGenreOnMovie = new ArrayList<>();
        ResponseEntity<GenreDTO[]> responseAllGenreOnMovie = restTemplate.getForEntity(urlAllGenreOnMovie, GenreDTO[].class);
        Collections.addAll(listAllGenreOnMovie, responseAllGenreOnMovie.getBody());

        Map<GenreDTO, Integer> mapAllGenreOnMovie = new TreeMap<>(new Comparator<GenreDTO>() {
            @Override
            public int compare(GenreDTO o1, GenreDTO o2) {
                if (o1.getId() >= o2.getId()) {
                    return 1;
                } else return -1;
            }
        });
        boolean loop;
        for (GenreDTO genre : listGenre) {
            loop = false;
            for (GenreDTO fk : listAllGenreOnMovie) {
                if (genre.getId() == fk.getId()) {
                    loop = true;
                    break;
                }
            }
            if (loop == true) {
                mapAllGenreOnMovie.put(genre, 1);
            } else mapAllGenreOnMovie.put(genre, 0);
        }

        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("error", error);
        model.addAttribute("mapAllGenreOnMovie", mapAllGenreOnMovie);
        model.addAttribute("listAllMembership", listAllMembership);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("editMV", editMV);
        model.addAttribute("format", format);
        model.addAttribute("inputDateFormat", inputDateFormat);
        model.addAttribute("listAllMovie", listAllMovie);
        return new ModelAndView("admin/editmovie");
    }

    @RequestMapping(method = RequestMethod.POST, path = "movie/edit/idMov={id}")
    public ModelAndView adminMovieEditPost(Model model, HttpSession session, @PathVariable("id") int id,
                                           @RequestParam("title") String title,
                                           @RequestParam("requiremember") String requiremember,
                                           @RequestParam("release") String release,
                                           @RequestParam("poster") MultipartFile poster,
                                           @RequestParam("hour") String hour,
                                           @RequestParam("min") String min,
                                           @RequestParam("second") String second,
                                           @RequestParam("status") String status,
                                           @RequestParam("detail") String detail,
                                           @RequestParam("trailer") String trailer,
                                           @RequestParam("movie") String movie,
                                           @RequestParam("genres") String[] genres,
                                           @RequestParam("posterchange") String posterchange,
                                           HttpServletResponse servletResponse) throws ParseException, IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        RestTemplate restTemplate = new RestTemplate();
        String error = "";
        String fs = "";
        String filenameL = "";

        loginResponse = (LoginResponse) session.getAttribute("loginAdminResponse");
        if (loginResponse == null) {
            session.setAttribute("oldAdminUrl", "/admin/movie/edit/idMov" + id);
            servletResponse.sendRedirect("/admin/login");
            return null;
        }
        loginAcc = (AccountDTO) session.getAttribute("loginAdminAcc");

        if (posterchange.equals("true")) {
            fs = poster.getOriginalFilename();
            filenameL = fs.substring(fs.indexOf("."), fs.length());
        }

        boolean stt = false;
        if (status.equals("1")) {
            stt = true;
        }

        if (listAllMovie == null) {
            DataCall dataCall = new DataCall();
            dataCall.dataCall(session);
            listAllMovie = (ArrayList<MovieDTO>) session.getAttribute("listAllMovie");
            listGenre = (ArrayList<GenreDTO>) session.getAttribute("listGenre");
            Collections.sort(listAllMovie, new Comparator<MovieDTO>() {
                @Override
                public int compare(MovieDTO o1, MovieDTO o2) {
                    if (o1.getId() < o2.getId())
                        return 1;
                    else return -1;
                }
            });
        }

        MovieDTO editMV = new MovieDTO();
        for (MovieDTO mv : listAllMovie) {
            if (mv.getId() == id) {
                editMV = mv;
            }
        }

        String urlAllMembership = "http://localhost:8080/api/membership/getAll";
        ArrayList<MembershipDTO> listAllMembership = new ArrayList<>();
        ResponseEntity<MembershipDTO[]> responseAllMembership = restTemplate.getForEntity(urlAllMembership, MembershipDTO[].class);
        Collections.addAll(listAllMembership, responseAllMembership.getBody());

        MembershipDTO member = new MembershipDTO();
        for (MembershipDTO mem : listAllMembership) {
            if (mem.getId() == Integer.parseInt(requiremember)) {
                member = mem;
                break;
            }
        }

        error += "Sửa phim thành công!!!";
        String urlEditMovie = "http://localhost:8080/api/movieDetail/editMovieDetail";
        MovieDTO movieDTO;
        if (posterchange.equals("true")) {
            movieDTO = new MovieDTO(id, title, filenameL, detail, stt, trailer, movie, inputDateFormat.parse(release), member, hour + ":" + min + ":" + second, 0);
        } else
            movieDTO = new MovieDTO(id, title, "false", detail, stt, trailer, movie, inputDateFormat.parse(release), member, hour + ":" + min + ":" + second, 0);
        HttpEntity<MovieDTO> requestEditMovie = new HttpEntity<>(movieDTO);
        ResponseEntity<MovieDTO> responseEditMovie = restTemplate.postForEntity(urlEditMovie, requestEditMovie, MovieDTO.class);

        if (posterchange.equals("true")) {
            HttpHeaders headersUploadImage = new HttpHeaders();
            headersUploadImage.setContentType(MediaType.MULTIPART_FORM_DATA);
            Resource muti = poster.getResource();
            MultiValueMap<String, Object> mapUp = new LinkedMultiValueMap<String, Object>();
            mapUp.add("image", muti);
            HttpEntity<MultiValueMap<String, Object>> requestUp = new HttpEntity<>(mapUp, headersUploadImage);
            String urlUpdateImage = "http://localhost:8080/uploadimage/" + "mv-" + id + "/221";
            ResponseEntity<JSONObject> responseUpdateImage = restTemplate.postForEntity(urlUpdateImage, requestUp, JSONObject.class);

            HttpHeaders headersGetImage = new HttpHeaders();
            headersGetImage.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", responseEditMovie.getBody().getPoster());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headersGetImage);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> reponseGet = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            movieDTO.setPoster(reponseGet.getBody().getUrl());
        } else movieDTO.setPoster(editMV.getPoster());
        listAllMovie.set(listAllMovie.indexOf(editMV), movieDTO);

        String urlAddFkGenre = "http://localhost:8080/api/fkGenre/addGenreForMovie/" + id;
        HttpEntity<String[]> requestAddFkGenre = new HttpEntity<>(genres);
        restTemplate.postForEntity(urlAddFkGenre, requestAddFkGenre, String.class);

        String urlAllGenreOnMovie = "http://localhost:8080/api/fkGenre/getAllGenre/" + id;
        ArrayList<GenreDTO> listAllGenreOnMovie = new ArrayList<>();
        ResponseEntity<GenreDTO[]> responseAllGenreOnMovie = restTemplate.getForEntity(urlAllGenreOnMovie, GenreDTO[].class);
        Collections.addAll(listAllGenreOnMovie, responseAllGenreOnMovie.getBody());

        Map<GenreDTO, Integer> mapAllGenreOnMovie = new TreeMap<>(new Comparator<GenreDTO>() {
            @Override
            public int compare(GenreDTO o1, GenreDTO o2) {
                if (o1.getId() >= o2.getId()) {
                    return 1;
                } else return -1;
            }
        });
        boolean loop;
        for (GenreDTO genre : listGenre) {
            loop = false;
            for (GenreDTO fk : listAllGenreOnMovie) {
                if (genre.getId() == fk.getId()) {
                    loop = true;
                    break;
                }
            }
            if (loop == true) {
                mapAllGenreOnMovie.put(genre, 1);
            } else mapAllGenreOnMovie.put(genre, 0);
        }


        model.addAttribute("loginAcc", loginAcc);
        model.addAttribute("error", error);
        model.addAttribute("mapAllGenreOnMovie", mapAllGenreOnMovie);
        model.addAttribute("listGenre", listGenre);
        model.addAttribute("editMV", movieDTO);
        model.addAttribute("format", format);
        model.addAttribute("inputDateFormat", inputDateFormat);
        model.addAttribute("listAllMembership", listAllMembership);
        model.addAttribute("listAllMovie", listAllMovie);
        return new ModelAndView("admin/editmovie");
    }
}
