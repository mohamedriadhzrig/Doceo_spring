package com.sopra.JpaRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopra.core.theme.Theme;

@Repository
public interface ThemeRepository extends JpaRepository<Theme,Long > {
	
	Theme findThemeByName(String name);
}
