package ge.mziuri.loanservice.repository;

import ge.mziuri.loanservice.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
}