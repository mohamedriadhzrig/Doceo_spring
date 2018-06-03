package com.sopra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopra.core.theme.Theme;

@Repository
public interface ThemeRepository extends JpaRepository<Theme,Long > {

	Theme save(Theme theme);
	
	Theme findThemeByName(String name);
}
