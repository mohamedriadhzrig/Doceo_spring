package com.sopra.services;

import java.util.List;

import com.sopra.entities.Theme;

public interface ThemeService {
	
	Theme save(Theme theme);
	
	void delete(Theme theme);
	
	void deleteById(Long id);
	
	Theme findThemeByName(String theme);
	
	Theme findThemeById(Long id);
	
	List<Theme> findAll();
	
	List<String> findAllTheme();
}
