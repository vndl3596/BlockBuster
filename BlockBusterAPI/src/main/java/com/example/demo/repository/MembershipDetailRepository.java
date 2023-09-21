package com.example.demo.repository;

import com.example.demo.model.Membership;
import com.example.demo.model.MembershipDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipDetailRepository extends JpaRepository<MembershipDetail, Integer> {
}
