package com.redpony.portfolioservice.repository;

import com.redpony.portfolioservice.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {
    Portfolio findByUsername(String username);
    boolean existsByUsername(String username);

    @Modifying
    @Transactional
    @Query("delete from Portfolio p where p.username = ?1")
    void deleteByUsername(String username);
}
