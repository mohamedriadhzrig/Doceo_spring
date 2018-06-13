package com.sopra.service.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.JpaRepositories.ArticleRepository;
import com.sopra.JpaRepositories.UserRepository;
import com.sopra.core.article.Article;
import com.sopra.core.article.ArticleService;
import com.sopra.core.rate.RateService;
import com.sopra.core.tag.Tag;
import com.sopra.core.tag.TagService;
import com.sopra.core.user.User;
import com.sopra.data.AdminArticleDataList;
import com.sopra.data.ArticleData;
import com.sopra.data.ArticleDataList;
import com.sopra.data.ProfileData;

@Transactional
@Service
public class ArticleServiceImplementation implements ArticleService {

	@Autowired // injection de dependence
	ArticleRepository articleRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TagService tagService;

	@Autowired
	RateService rateService;

	@Override
	public void save(Article article) {

		articleRepository.save(article);

	}

	@Override
	public void remove(Article article) {
		articleRepository.delete(article);
		/*
		 * System.out.println("*****1****"); for (Tag t : article.getTags()) {
		 * System.out.println(t.getArticles().size() + "*****2****" + t.getName());
		 * 
		 * if (t.getArticles().size() == 1) { System.out.println("*****3****" +
		 * t.getArticles().size()); Tag tag =
		 * tagService.findTagByName(t.getName()).get(); tagService.remove(tag);
		 * 
		 * } System.out.println("*****4****");
		 * 
		 * } System.out.println("*****5****");
		 * 
		 * 
		 * System.out.println("*****6****");
		 */

	}

	@Override
	public ArticleDataList findArticles(String author, User user) {

		List<Article> list = new ArrayList<Article>();

		list.addAll(articleRepository
				.findByUserIsAndStatutIsOrderByCreatedAtDesc(userRepository.findUserByUsername(author), "valide"));
		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(a.getUser().getUsername() == user.getUsername() || a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating = 0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setAdmin(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}

		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;
	}

	@Override
	public Article findArticleBySlug(String slug) {

		return articleRepository.findArticleBySlug(slug);
	}

	@Override
	public ArticleDataList findArticlesByTag(String tag, User user) {
		Optional<Tag> thisTag = tagService.findTagByName(tag);
		List<Article> list = new ArrayList<Article>();
		if (thisTag.isPresent())
			list.addAll(articleRepository.findArticlesByTag(thisTag.get()));

		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(a.getUser().getUsername() == user.getUsername() || a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating = 0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setAdmin(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}

		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;
	}

	@Override
	public ArticleDataList findFavoriteArticles(String uername, User user) {
		List<Article> list = new ArrayList<Article>();
		list.addAll(userRepository.findUserByUsername(uername).getFavoriteArticles());
		Collections.sort(list, (o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(a.getUser().getUsername() == user.getUsername() || a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating = 0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setAdmin(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}
		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;
	}

	@Override
	public ArticleDataList findAllValide(User user) {
		List<Article> list = new ArrayList<Article>();
		list.addAll(articleRepository.findByStatutIsOrderByCreatedAtDesc("valide"));

		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(a.getUser().getUsername() == user.getUsername() || a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating = 0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setAdmin(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}

		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;

	}

	@Override
	public AdminArticleDataList findAllInvalide() {
		List<Article> list = new ArrayList<Article>();
		list.addAll(articleRepository.findByStatutIsOrderByCreatedAtAsc("invalide"));

		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(false);
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating = 0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setAdmin(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}

		Date date1 = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(date1);
		c.add(Calendar.DATE, -7);
		Date date2 = c.getTime();
		AdminArticleDataList articleDataList = new AdminArticleDataList(list1, list1.size(), articleRepository.count(),
				articleRepository.countByCreatedAtBetween(date2, date1));
		return articleDataList;

	}

	@Override
	public ArticleDataList findValidArticlesByUser(String author, User user) {
		List<Article> list = new ArrayList<Article>();
		list.addAll(articleRepository
				.findByUserIsAndStatutIsOrderByCreatedAtDesc(userRepository.findUserByUsername(author), "valide"));
		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(a.getUser().getUsername() == user.getUsername() || a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating = 0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setAdmin(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}

		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;
	}

	@Override
	public ArticleDataList getFeed(User user) {
		List<Article> list = new ArrayList<Article>();
		if (user.getThemes().isEmpty()) {
			return this.findAllValide(user);
		}

		list.addAll(articleRepository.findByThemeInAndStatutIsOrderByCreatedAtDesc(user.getThemes(), "valide"));

		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(a.getUser().getUsername() == user.getUsername() || a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating = 0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setAdmin(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}
		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;
	}

}
