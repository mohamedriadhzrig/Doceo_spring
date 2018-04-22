package com.sopra.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopra.core.tag.TagService;

@RestController
@RequestMapping(path = "tags")
public class TagsApi {

	@Autowired
	TagService tagService;

	@GetMapping
	public ResponseEntity getTags() {
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("tags", tagService.findAll());
			}
		});
	}
}
