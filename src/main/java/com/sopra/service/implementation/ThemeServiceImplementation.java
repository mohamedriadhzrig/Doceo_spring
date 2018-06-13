package com.sopra.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.JpaRepositories.ThemeRepository;
import com.sopra.core.theme.Theme;
import com.sopra.core.theme.ThemeService;

@Transactional
@Service
public class ThemeServiceImplementation implements ThemeService {

	@Autowired
	ThemeRepository themeRepository;

	@Override
	public Theme save(Theme theme) {
		return themeRepository.save(theme);
	}

	@Override
	public void delete(Theme theme) {
		themeRepository.delete(theme);

	}

	@Override
	public void deleteById(Long id) {
		themeRepository.delete(id);

	}

	@Override
	public Theme findThemeByName(String name) {

		return themeRepository.findThemeByName(name);
	}

	@Override
	public Theme findThemeById(Long id) {

		return themeRepository.findOne(id);
	}

	@Override
	public List<Theme> findAll() {

		return themeRepository.findAll();
	}

	@Override
	public List<String> findAllTheme() {
		List<String> list = new ArrayList<String>();
		for (Theme t : themeRepository.findAll()) {
			list.add(t.getName());
		}
		return list;
	}

}
