package com.sopra.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopra.entities.Theme;

@Repository
public interface ThemeRepository extends JpaRepository<Theme,Long > {
	
	Theme findThemeByName(String name);
}
