package com.example.demo.service;


import com.example.demo.dto.AccountDTO;
import com.example.demo.dto.AccountPage;
import com.example.demo.dto.MembershipDTO;
import com.example.demo.exception.MailException;
import com.example.demo.exception.UsernameExitException;
import com.example.demo.model.Account;
import com.example.demo.model.AccountRole;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MembershipService {
    List<MembershipDTO> getAllMembershipDetail();
    MembershipDTO getMembershipById(int id);
}
