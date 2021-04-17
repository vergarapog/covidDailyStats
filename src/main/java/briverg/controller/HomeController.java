package briverg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import briverg.services.CovidDataService;

@Controller
public class HomeController {

	@Autowired
	CovidDataService covidService;
	
	@GetMapping("/")
	public String home(Model model) {
		
		model.addAttribute("locStats", covidService.getAllStats());
		model.addAttribute("totalAll", covidService.getTotalAll());
		model.addAttribute("totalNew", covidService.getTotalAllPrevDay());
		
		
		
		return "home";
	}
	
}
