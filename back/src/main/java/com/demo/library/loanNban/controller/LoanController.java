package com.demo.library.loanNban.controller;

import com.demo.library.loanNban.dto.LoanDto;
import com.demo.library.loanNban.entity.Loan;
import com.demo.library.loanNban.mapper.LoanMapper;
import com.demo.library.loanNban.service.LoanService;
import com.demo.library.response.ResponseCreator;
import com.demo.library.response.dto.SingleResponseDto;
import com.demo.library.security.service.JWTAuthService;
import com.demo.library.user.entity.User;
import com.demo.library.user.service.UserService;
import com.demo.library.utils.UriCreator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;



@RestController
@RequestMapping("/loans")
@Validated
@RequiredArgsConstructor
public class LoanController {
    private final static String LOAN_DEFAULT_URL = "/loans";
    private final LoanService service;
    private final LoanMapper mapper;
    private final JWTAuthService jwtAuthService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> postLoan(@Valid @RequestBody LoanDto.Post post) {
        User user = userService.findByEmail(jwtAuthService.getEmail());
        Loan loan = mapper.postToLoan(post,user);
        service.create(loan);

        URI location = UriCreator.createUri(LOAN_DEFAULT_URL, loan.getId());
        return ResponseCreator.created(location);
    }

    @PatchMapping
    public ResponseEntity<SingleResponseDto<LoanDto.Response>> patchLoan(@Valid @RequestBody LoanDto.Patch patch) {

        Loan loanRequest = mapper.patchToLoan(patch);
        Loan updatedLoan = service.update(loanRequest);

        LoanDto.Response responseDto = mapper.loanToResponse(updatedLoan);
        return ResponseCreator.single(responseDto);
    }
    @GetMapping("/{loan_id}")
    public ResponseEntity<SingleResponseDto<LoanDto.Response>> getLoan(@PathVariable("loan_id") Long Id) {

        Loan loan = service.findAndVerifyLoanById(Id);
        LoanDto.Response responseDto = mapper.loanToResponse(loan);

        return ResponseCreator.single(responseDto);
    }
    @GetMapping
    public ResponseEntity getLoans(@RequestParam Long userId) {

        List<Loan> loans = service.findLoans(userId);
        List<LoanDto.Response> responses = mapper.loansToLoanResponses(loans);

        return ResponseCreator.list(responses);
    }

    @DeleteMapping("/{loan-id}")
    public ResponseEntity deleteLoan(@PathVariable("loan-id")Long Id) {
        service.delete(Id);
        return ResponseCreator.deleted();
    }
}

