package ge.mziuri.loanservice.service;

import ge.mziuri.loanservice.entity.LoanEntity;
import ge.mziuri.loanservice.model.LoanRequest;

public interface LoanService {
    LoanEntity borrow(LoanRequest loanRequest);

    LoanEntity returnBook(Long id);
}
