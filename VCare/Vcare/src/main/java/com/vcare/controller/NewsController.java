package com.vcare.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.vcare.beans.Hospital;
import com.vcare.beans.News;
import com.vcare.service.HospitalService;
import com.vcare.service.NewsService;

@Controller
@RequestMapping("/news")
public class NewsController {

	@Autowired
	NewsService newsservice;
   @Autowired
   HospitalService hospitalService;
	@GetMapping("/viewAllNews")
	public String viewHomePages(Model model) {
		List<Hospital> hospList = hospitalService.getAllHospitals();
		model.addAttribute("hospitalsList", hospList);
		List<News> newslist = newsservice.getAllNews();
		model.addAttribute("newslist", newslist);
		return "indexnews";
	}

	
	@GetMapping("/newsdetails/{id}")
	public String displayNews(Model model, @PathVariable("id") int NewsId) {
		News obj = newsservice.GetNewsById(NewsId);
		model.addAttribute("newspage", obj);
		List<News> newslists = newsservice.getLatestNews();
		model.addAttribute("newsLists", newslists);
		return "newspage";
		
	}

	@GetMapping("/newstable")
	public String viewalldepartment(Model model) {
		model.addAttribute("newslist", newsservice.getAllNews());
		return "Newslist";
	}

	@GetMapping("/deletenews/{NewsId}")
	public String deleteNews(Model model, @PathVariable(value = "NewsId") int NewsId) {
		System.out.println("Hi delete");
		newsservice.deleteNewsById(NewsId);
		System.out.println("delete operation is invoked");
		model.addAttribute("newslist", newsservice.getAllNews());
		return "Newslist";
	}

	@GetMapping("/addnews")
	public String showNewNewsForm(Model model) {
		News news = new News();
		model.addAttribute("objnews", news);
		return "AddNews";
	}

	/*
	 * @RequestMapping(value = "/savenews", method = RequestMethod.POST) public
	 * String addNews(Model model,@RequestParam("file") MultipartFile
	 * file,@RequestParam("file") MultipartFile file1 ,News newsObj) throws
	 * IOException { newsObj.setCreated(LocalDate.now());
	 * newsObj.setNewsImage(Base64.getEncoder().encodeToString(file.getBytes()));
	 * newsservice.addSaveNews(newsObj); List<News> newslist =
	 * newsservice.getAllNews(); model.addAttribute("newslist", newslist); return
	 * "redirect:/news/newstable";
	 * 
	 * }
	 */
	
	@RequestMapping(value = "/savenews", method = RequestMethod.POST)
	public String addNews(Model model,@RequestParam("file") MultipartFile file ,@RequestParam("file1") MultipartFile file1 ,News newsObj) throws IOException {
	newsObj.setCreated(LocalDate.now());
	newsObj.setNewsImage(Base64.getEncoder().encodeToString(file.getBytes()));
	newsObj.setVideo(Base64.getEncoder().encodeToString(file1.getBytes()));
	newsservice.SaveMultimedia(file, newsObj);
	newsservice.SaveMultimedia(file1, newsObj);
	List<News> newslist = newsservice.getAllNews();
	model.addAttribute("newslist", newslist);

	return "redirect:/news/newstable";
	}

	@GetMapping("/editnews/{NewsId}")
	public String editNews(Model model, @PathVariable(value = "NewsId") int NewsId) {
		News NewsObj = newsservice.GetNewsById(NewsId);
		model.addAttribute("objnews", NewsObj);
		model.addAttribute("NewsId", NewsId);
		return "AddNews";
	}

}
