package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ScheduleController {

	@Autowired
	private CourseOccurrenceService courseOccurrenceService;


	@GetMapping("/schedule")
	public String getSchedule(Model model, HttpSession session, @RequestParam(value = "id", required = false) String id, @RequestParam(value = "year", required = false) String yearParam, @RequestParam(value = "month", required = false) String monthParam, @RequestParam(value = "day", required = false) String dayParam) {

		String role = (String) session.getAttribute("role");
		// check the user authenticated
		if (role == null || session.getAttribute("user") == null) {
			return "login";
		}

		// check if he has the right to get what he's asking for
		if (role.equals(Student.class.getName()) || role.equals(Professor.class.getName())) {
			Person user = (Person) session.getAttribute("user");
			if (id == null || id.isBlank()) {
				id = user.getPersonNumber();
			} // if no schedule specified, set to his own
			if (!user.getPersonNumber().equals(id)) {
				model.addAttribute("errorMessage", "Vous n'avez pas l'autorisation d'accéder à cette ressource");
				return "schedule";
			}
		} else if (role.equals(Admin.class.getName())) {
			Person user = (Person) session.getAttribute("user");
			if (id == null) {
				id = user.getPersonNumber();
			}
		} else {
			model.addAttribute("errorMessage", "role inconnu, merci de vous reconnecter");
			return "login";
		}

		if (yearParam == null && monthParam == null && dayParam == null) {
			dayParam = String.format("%02d", LocalDate.now().getDayOfMonth());
			monthParam = String.format("%02d", LocalDate.now().getMonthValue());
			yearParam = String.format("%04d", LocalDate.now().getYear());
		} else if (yearParam == null || monthParam == null || dayParam == null) {
			model.addAttribute("errorMessage", "La date est invalide");
			return "schedule";
		}

		int year = Integer.parseInt(yearParam);
		int month = Integer.parseInt(monthParam);
		int day = Integer.parseInt(dayParam);

		LocalDate monday;
		try {
			LocalDate date = LocalDate.of(year, month, day);

			DayOfWeek dayOfWeek = date.getDayOfWeek();
			if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
				monday = date.with(DayOfWeek.MONDAY).plusWeeks(1);
			} else {
				monday = date.with(DayOfWeek.MONDAY);
			}
		} catch (Exception e) {
			model.addAttribute("errorMessage", "La date est invalide");
			return "schedule";
		}



		List<LocalDate> dates = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			dates.add(monday.plusDays(i));
		}
		Map<String, List<Map<String, String>>> schedule = courseOccurrenceService.getCoursesByPersonNumberAndDays(id, dates);
		Map<String, Integer> days = new LinkedHashMap<>();
		days.put(DayOfWeek.MONDAY.toString(), monday.getDayOfMonth());
		days.put(DayOfWeek.TUESDAY.toString(), monday.plusDays(1).getDayOfMonth());
		days.put(DayOfWeek.WEDNESDAY.toString(), monday.plusDays(2).getDayOfMonth());
		days.put(DayOfWeek.THURSDAY.toString(), monday.plusDays(3).getDayOfMonth());
		days.put(DayOfWeek.FRIDAY.toString(), monday.plusDays(4).getDayOfMonth());

		// Trier les jours par ordre chronologique
		List<Map.Entry<String, Integer>> sortedDays = new ArrayList<>(days.entrySet());
		sortedDays.sort(Map.Entry.comparingByValue());

		// Reconstituer le Map trié
		Map<String, Integer> sortedDaysMap = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : sortedDays) {
			sortedDaysMap.put(entry.getKey(), entry.getValue());
		}

		model.addAttribute("schedule", schedule);
		model.addAttribute("days", sortedDaysMap);
		return "schedule";
	}
}