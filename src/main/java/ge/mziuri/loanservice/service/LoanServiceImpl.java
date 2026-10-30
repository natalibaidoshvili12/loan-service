package ge.mziuri.loanservice.service;

import ge.mziuri.loanservice.entity.LoanEntity;
import ge.mziuri.loanservice.exception.BookNotFoundException;
import ge.mziuri.loanservice.exception.NoAvailableCopiesException;
import ge.mziuri.loanservice.external.BookClient;
import ge.mziuri.loanservice.model.LoanRequest;
import ge.mziuri.loanservice.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository repository;
    private final BookClient bookClient;

    @Override
    public LoanEntity borrow(LoanRequest request) {
        // 1. Ask book-service for a copy FIRST ...
        try {
            bookClient.reserve(request.bookId());
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book " + request.bookId() + " not found");
        } catch (NoAvailableCopiesException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No copies available");
        }
        // 2. ... and save the loan only once the reservation succeeded.
        LoanEntity loanEntity = LoanEntity.builder()
                .bookId(request.bookId())
                .memberName(request.memberName())
                .build();
        return repository.save(loanEntity);
    }

    @Override
    public LoanEntity returnBook(Long id) {
        LoanEntity loan = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan " + id + " not found"));

        if (loan.isReturned()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Loan " + id + " already returned");
        }

        loan.setReturned(true);
        repository.save(loan);
        bookClient.release(loan.getBookId());
        return loan;
    }

}
