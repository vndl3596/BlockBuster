package com.example.demo.service;


import com.example.demo.dto.MembershipBuyHistoryDTO;
import com.example.demo.dto.MembershipDTO;

import java.util.List;

public interface MembershipBuyHistoryService {
    String saveBuyHistory(int idAcc, int idMembershipDetail, int voucherId) throws Exception;
    List<MembershipBuyHistoryDTO> getAllBuyHistory() throws Exception;
    List<MembershipBuyHistoryDTO> getAllBuyHistoryByUsername(String username);
}
