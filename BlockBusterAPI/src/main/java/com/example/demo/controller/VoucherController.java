package com.example.demo.controller;

import com.example.demo.dto.VoucherDTO;
import com.example.demo.service.VoucherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/voucher")
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping("/getAllVoucher")
    public ResponseEntity<List<VoucherDTO>> getAllVoucher() {
        return new ResponseEntity<>(voucherService.getAllVouchers(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<VoucherDTO> createVoucher(@RequestBody VoucherDTO voucherDTO) {
        return new ResponseEntity<>(voucherService.createVoucher(voucherDTO), HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<VoucherDTO> editVoucher(@RequestBody VoucherDTO voucherDTO) {
        return new ResponseEntity<>(voucherService.editVoucher(voucherDTO), HttpStatus.OK);
    }

    @GetMapping("/getVoucherById/{id}")
    public ResponseEntity<VoucherDTO> getAllVoucher(@PathVariable("id") int id) {
        return new ResponseEntity<>(voucherService.getVoucherById(id), HttpStatus.OK);
    }

    @GetMapping("/getVoucherByCode/{code}")
    public ResponseEntity<VoucherDTO> getVoucherByCode(@PathVariable("code") String code) {
        return new ResponseEntity<>(voucherService.getVoucherByCode(code), HttpStatus.OK);
    }

    @GetMapping("/getVoucherByName/{name}")
    public ResponseEntity<VoucherDTO> getVoucherByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(voucherService.getVoucherByName(name), HttpStatus.OK);
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<String> removeVoucherById(@PathVariable int id) {
        return new ResponseEntity<>(voucherService.deleteVoucher(id), HttpStatus.OK);
    }

}
