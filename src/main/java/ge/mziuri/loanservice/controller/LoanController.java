package ge.mziuri.loanservice.controller;

import ge.mziuri.loanservice.entity.LoanEntity;
import ge.mziuri.loanservice.model.LoanRequest;
import ge.mziuri.loanservice.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LoanEntity> createLoan(@RequestBody LoanRequest loanRequest) {
        return new ResponseEntity<>(loanService.borrow(loanRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<LoanEntity> returnBook(@PathVariable Long id) {
        return new ResponseEntity<>(loanService.returnBook(id), HttpStatus.OK);
    }
}