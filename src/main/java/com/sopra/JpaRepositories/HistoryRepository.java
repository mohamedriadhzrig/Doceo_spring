package com.sopra.JpaRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopra.core.history.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>{

}
