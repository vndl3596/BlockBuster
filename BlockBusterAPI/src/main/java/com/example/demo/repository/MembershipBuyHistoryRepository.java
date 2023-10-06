package com.example.demo.repository;

import com.example.demo.model.FKMembership;
import com.example.demo.model.Key.FkAccountMemberKey;
import com.example.demo.model.MembershipBuyHistory;
import com.example.demo.model.MembershipDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipBuyHistoryRepository extends JpaRepository<MembershipBuyHistory, Integer> {
}
