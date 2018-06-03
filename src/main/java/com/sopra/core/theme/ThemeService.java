package com.sopra.core.theme;

import java.util.List;

public interface ThemeService {
	
	Theme save(Theme theme);
	
	void delete(Theme theme);
	
	void deleteById(Long id);
	
	Theme findThemeByName(String name);
	
	Theme findThemeById(Long id);
	
	List<Theme> findAll();
	
	List<String> findAllTheme();
}
