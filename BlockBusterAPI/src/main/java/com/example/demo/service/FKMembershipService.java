package com.example.demo.service;

import com.example.demo.dto.FKMembershipDTO;
import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.dto.MovieDirectorDTO;
import com.example.demo.model.FKMembership;

import java.util.List;

public interface FKMembershipService {

    List<FKMembershipDTO> getFkMembershipByUsername(String username);

}
