package com.example.demo.repository;

import com.example.demo.model.UserHistory;
import com.example.demo.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
}
