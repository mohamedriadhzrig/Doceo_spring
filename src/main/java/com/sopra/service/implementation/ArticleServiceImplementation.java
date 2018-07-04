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

import com.sopra.DAO.ArticleRepository;
import com.sopra.DAO.UserRepository;
import com.sopra.data.AdminArticleDataList;
import com.sopra.data.ArticleData;
import com.sopra.data.ArticleDataList;
import com.sopra.data.ProfileData;
import com.sopra.entities.Article;
import com.sopra.entities.Project;
import com.sopra.entities.Tag;
import com.sopra.entities.Team;
import com.sopra.entities.Theme;
import com.sopra.entities.User;
import com.sopra.services.ArticleService;
import com.sopra.services.RateService;
import com.sopra.services.TagService;
import com.sopra.services.ThemeService;

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
	ThemeService themeService;

	@Autowired
	RateService rateService;

	@Override
	public void save(Article article) {

		articleRepository.save(article);

	}

	@Override
	public void remove(Article article) {
		articleRepository.delete(article);

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

	@Override
	public ArticleDataList findArticlesByTagAndTheme(String tag, String theme, User user) {

		Optional<Tag> thisTag = tagService.findTagByName(tag);
		Theme th = themeService.findThemeByName(theme);
		List<Article> list = new ArrayList<Article>();
		list.addAll(articleRepository.findArticlesByTagByTheme(th, thisTag.get()));

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
	public ArticleDataList findArticlesByTheme(String theme, User user) {
		Theme th = themeService.findThemeByName(theme);
		List<Article> list = new ArrayList<Article>();
		list.addAll(articleRepository.findByThemeAndStatutIsOrderByCreatedAtDesc(th, "valide"));

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
	public ArticleDataList findArticlesBy(String text, Project project, Team team, Theme theme, Tag tag, User user) {
		List<Article> list = new ArrayList<Article>();

		switch (getCase(text, project, team, theme, tag)) {
		case 0:
			list.addAll(articleRepository.findArticlesByAll(tag, text, theme, project, team));
			break;
		case 1:
			list.addAll(articleRepository.findByBodyLikeAndThemeAndTeamAndProjectAndStatutIsOrderByCreatedAtDesc(text,
					theme, team, project, "valide"));
			break;
		case 2:
			list.addAll(articleRepository.findArticlesBythemeByTagByTeamBytext(team, theme, tag, text));
			break;
		case 3:
			list.addAll(articleRepository.findArticlesByProjectByTagByTeamBytext(team, tag, project, text));
			break;
		case 4:
			list.addAll(articleRepository.findArticlesBythemeByTagByProjectBytext(project, theme, tag, text));
			break;
		case 5:
			list.addAll(articleRepository.findArticlesBythemeByTagByProjectByteam(tag, theme, project, team));
			break;
		case 6:
			list.addAll(articleRepository.findByBodyLikeAndThemeAndTeamAndStatutIsOrderByCreatedAtDesc(text, theme,
					team, "valide"));
			break;
		case 7:
			list.addAll(articleRepository.findByBodyLikeAndProjectAndTeamAndStatutIsOrderByCreatedAtDesc(text, project,
					team, "valide"));
			break;
		case 8:
			list.addAll(articleRepository.findByBodyLikeAndThemeAndProjectAndStatutIsOrderByCreatedAtDesc(text, theme,
					project, "valide"));
			break;
		case 9:list.addAll(articleRepository.findByThemeAndTeamAndProjectAndStatutIsOrderByCreatedAtDesc(theme, team, project, "valide"))
			;
			break;
		case 10:list.addAll(articleRepository.findArticlesByTagByTeamBytext(team, tag, text))
			;
			break;
		case 11:list.addAll(articleRepository.findArticlesByTagByThemeBytext(theme, tag, text))
			;
			break;
		case 12:list.addAll(articleRepository.findArticlesByTagByThemeByTeam(theme, tag, team))
			;
			break;
		case 13:list.addAll(articleRepository.findArticlesByTagByProjectByText(project, tag, text))
			;
			break;
		case 14:list.addAll(articleRepository.findArticlesByTagByProjectByTeam(project, tag, team))
			;
			break;
		case 15:list.addAll(articleRepository.findArticlesByTagByProjectByTheme(project, tag, theme))
			;
			break;
		case 16:list.addAll(articleRepository.findByBodyLikeAndTeamIsAndStatutIsOrderByCreatedAtDesc(text, team, "valide"))
			;
			break;
		case 17:list.addAll(articleRepository.findByBodyLikeAndThemeIsAndStatutIsOrderByCreatedAtDesc(text, theme, "valide"))
			;
			break;
		case 18:list.addAll(articleRepository.findByThemeAndTeamAndStatutIsOrderByCreatedAtDesc(theme, team, "valide"))
			;
			break;
		case 19:list.addAll(articleRepository.findByBodyLikeAndProjectIsAndStatutIsOrderByCreatedAtDesc(text, project, "valide"))
			;
			break;
		case 20:list.addAll(articleRepository.findByProjectAndTeamAndStatutIsOrderByCreatedAtDesc(project, team, "valide"))
			;
			break;
		case 21:list.addAll(articleRepository.findByThemeAndProjectAndStatutIsOrderByCreatedAtDesc(theme, project, "valide"))
			;
			break;
		case 22:list.addAll(articleRepository.findArticlesByTagBytext(text, tag))
			;
			break;
		case 23:list.addAll(articleRepository.findArticlesByTagByTeam(team, tag))
			;
			break;
		case 24:list.addAll(articleRepository.findArticlesByTagByProject(project, tag))
			;
			break;
		case 25:list.addAll(articleRepository.findArticlesByTagByTheme(theme, tag))
			;
			break;
		case 26:list.addAll(articleRepository.findArticlesByTag(tag))
			;
			break;
		case 27:list.addAll(articleRepository.findByProjectIsAndStatutIsOrderByCreatedAtDesc(project, "valide"))
			;
			break;
		case 28:list.addAll(articleRepository.findByThemeIsAndStatutIsOrderByCreatedAtDesc(theme, "valide"))
			;
			break;
		case 29:list.addAll(articleRepository.findByTeamIsAndStatutIsOrderByCreatedAtDesc(team, "valide"))
			;
			break;
		case 30:list.addAll(articleRepository.findByBodyLikeOrderByCreatedAtDesc(text, "valide"))
			;
			break;
		case 31:list.addAll(articleRepository.findByStatutIsOrderByCreatedAtDesc("valide"))
			;
			break;
		}

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

	private int getCase(String text, Project project, Team team, Theme theme, Tag tag) {

		if (text != null && project != null && team != null && theme != null && tag != null)
			return 0;

		if (text != null && project != null && team != null && theme != null && tag == null)
			return 1;
		if (text != null && project == null && team != null && theme != null && tag != null)
			return 2;
		if (text != null && project != null && team != null && theme == null && tag != null)
			return 3;
		if (text != null && project != null && team == null && theme != null && tag != null)
			return 4;
		if (text == null && project != null && team != null && theme != null && tag != null)
			return 5;

		if (text != null && project == null && team != null && theme != null && tag == null)
			return 6;
		if (text != null && project != null && team != null && theme == null && tag == null)
			return 7;
		if (text != null && project != null && team == null && theme != null && tag == null)
			return 8;
		if (text == null && project != null && team != null && theme != null && tag == null)
			return 9;
		if (text != null && project == null && team != null && theme == null && tag != null)
			return 10;
		if (text != null && project == null && team == null && theme != null && tag != null)
			return 11;
		if (text == null && project == null && team != null && theme != null && tag != null)
			return 12;
		if (text != null && project != null && team == null && theme == null && tag != null)
			return 13;
		if (text == null && project != null && team != null && theme == null && tag != null)
			return 14;
		if (text == null && project != null && team == null && theme != null && tag != null)
			return 15;

		if (text != null && project == null && team != null && theme == null && tag == null)
			return 16;
		if (text != null && project == null && team == null && theme != null && tag == null)
			return 17;
		if (text == null && project == null && team != null && theme != null && tag == null)
			return 18;
		if (text != null && project != null && team == null && theme == null && tag == null)
			return 19;
		if (text == null && project != null && team != null && theme == null && tag == null)
			return 20;
		if (text == null && project != null && team == null && theme != null && tag == null)
			return 21;
		if (text != null && project == null && team == null && theme == null && tag != null)
			return 22;
		if (text == null && project == null && team != null && theme == null && tag != null)
			return 23;
		if (text == null && project != null && team == null && theme == null && tag != null)
			return 24;
		if (text == null && project == null && team == null && theme != null && tag != null)
			return 25;

		if (text == null && project == null && team == null && theme == null && tag != null)
			return 26;
		if (text == null && project != null && team == null && theme == null && tag == null)
			return 27;
		if (text == null && project == null && team == null && theme != null && tag == null)
			return 28;
		if (text == null && project == null && team != null && theme == null && tag == null)
			return 29;
		if (text != null && project == null && team == null && theme == null && tag == null)
			return 30;

		if (text == null && project == null && team == null && theme == null && tag == null)
			return 31;

		return 31;

	}

}
