package com.example.demo.controller;

import com.example.demo.dto.MembershipBuyHistoryDTO;
import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.dto.UserHistoryDTO;
import com.example.demo.dto.VoucherDTO;
import com.example.demo.service.MembershipBuyHistoryService;
import com.example.demo.service.UserHistoryService;
import com.example.demo.service.VoucherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping("/getAll")
    public ResponseEntity<List<VoucherDTO>> getAllVoucher() throws Exception {
        return new ResponseEntity<>(voucherService.getAllVoucher(), HttpStatus.OK);
    }

    @GetMapping("/getById/idVoucher={id}")
    public ResponseEntity<VoucherDTO> getAllVoucherById(@PathVariable int id) throws Exception {
        return new ResponseEntity<>(voucherService.getVoucherById(id), HttpStatus.OK);
    }

    @PostMapping("/addVoucher")
    public ResponseEntity<VoucherDTO> addVoucher(@RequestBody VoucherDTO voucherDTO) throws Exception {
        return new ResponseEntity<>(voucherService.addVoucher(voucherDTO), HttpStatus.OK);
    }

    @PostMapping("/editVoucher")
    public ResponseEntity<VoucherDTO> editVoucher(@RequestBody VoucherDTO voucherDTO) throws Exception {
        return new ResponseEntity<>(voucherService.editVoucher(voucherDTO), HttpStatus.OK);
    }

    @GetMapping("/deleteVoucher/{id}")
    public ResponseEntity<String> deleteVoucherById(@PathVariable int id) throws Exception {
        return new ResponseEntity<>(voucherService.deleteVoucherById(id), HttpStatus.OK);
    }

    @GetMapping("/getVoucherByMemDeId/{id}")
    public ResponseEntity<VoucherDTO> getVoucherByMemDeId(@PathVariable int id) throws Exception {
        return new ResponseEntity<>(voucherService.getVoucherByMemDeId(id), HttpStatus.OK);
    }
}
