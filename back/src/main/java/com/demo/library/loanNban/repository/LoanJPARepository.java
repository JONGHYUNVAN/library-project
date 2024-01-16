package com.demo.library.loanNban.repository;
import com.demo.library.loanNban.entity.Loan;
import com.demo.library.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanJPARepository extends JpaRepository<Loan, Long> {
        Optional<Loan> findById(Long Id);
        List<Loan> findLoansByUser(User user);
}
