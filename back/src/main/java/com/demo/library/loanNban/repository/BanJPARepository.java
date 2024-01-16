package com.demo.library.loanNban.repository;

import com.demo.library.loanNban.entity.Ban;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanJPARepository extends JpaRepository<Ban, Long> {
}
