package com.vcare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vcare.beans.AmbulanceDriverAssosiation;
import com.vcare.beans.BookingAmbulance;
import com.vcare.beans.Employees;
import com.vcare.beans.HospitalBranch;
import com.vcare.service.AmbulanceDriverService;
import com.vcare.service.BookingAmbService;
import com.vcare.service.HospitalBranchService;

@Controller
public class BookingAmbulanceController {
	
	@Autowired
	BookingAmbService bookingAmbService;
	@Autowired
	AmbulanceDriverService ambulanceDriverService;
	@Autowired
	HospitalBranchService hospitalBranchService;
	
	
	
	@GetMapping("/BookingList")
	public String getbyid(Model model,BookingAmbulance BookingAmbulance) {
		List<BookingAmbulance> Bookingambulancelist = bookingAmbService.getAllBookings();
		model.addAttribute("Bookingambulancelist", Bookingambulancelist);
		return "BookingAmblist";
		
}
	
	 @RequestMapping(value=("/bookingform/{ambid}"), method=RequestMethod.GET)
	  public String Getbyid(Model model,@PathVariable("ambid")int ambid,BookingAmbulance BookingAmbulance) {
		 System.out.println("::::::::::::::"+ambid);
		 AmbulanceDriverAssosiation  ambulancedriverob = ambulanceDriverService.getById(ambid);
		 
		  model.addAttribute("ambulancetype", ambulancedriverob.getAmbulancetype());
		  System.out.println("this is bookinga amabuybiwjx"+ambulancedriverob.getAmbulancetype());
	  model.addAttribute("drivername", ambulancedriverob.getDriverId());
	  System.out.println("this is bookinga amabuybiwjx"+ambulancedriverob.getAmbulancetype());
	  model.addAttribute("BookingAmbulance",BookingAmbulance);
	  return"bookingambulance";
	  
	  }
	  @PostMapping("/savebookings")
	  public String updatebookingfrom(Model model,BookingAmbulance BookingAmbulance) {
 System.out.println("this mthod ");
 model.addAttribute("BookingAmbulance", BookingAmbulance);
 BookingAmbulance.setIsactive('y');
 bookingAmbService.addBookings(BookingAmbulance);
 List<BookingAmbulance> Bookingambulancelist = bookingAmbService.getAllBookings();
	model.addAttribute("Bookingambulancelist", Bookingambulancelist);
 return "redirect:/BookingList";
	 }
	  @GetMapping("/deleteBooking/{id}")
		public String deleteBooking(Model model, @PathVariable int id) {
			System.out.println("inside deleteHospitalBranch id:::" + id);
			bookingAmbService.deleteBookingsById(id);
			List<BookingAmbulance> Bookingambulancelist = bookingAmbService.getAllBookings();
			model.addAttribute("Bookingambulancelist", Bookingambulancelist);
			 return "redirect:/BookingList";
			 
	  }
	  @GetMapping("/editbooking/{bookingId}")
		public String getById(Model model, @PathVariable("bookingId") int bookingId, BookingAmbulance BookingAmbulance) {
		  System.out.println("::::::::::::"+BookingAmbulance.getBookingId());
		  BookingAmbulance BookingAmbulance2 = bookingAmbService.getById(bookingId);
		  System.out.println("::::::::::::"+BookingAmbulance2.getToAddress());
		  model.addAttribute("BookingAmbulance", BookingAmbulance);
		  model.addAttribute("ambulancetype", BookingAmbulance2.getAmbulancetype());
		  model.addAttribute("drivername", BookingAmbulance2.getDriverName());
		  BookingAmbulance.setIsactive('y');
		  model.addAttribute("BookingAmbulance", BookingAmbulance2);
		  System.out.println("this is bookinga amabuybiwjx");
			System.out.println("inside getHospitalbranchId id is:::" + bookingAmbService.getById(bookingId));
			
			return "bookingambulance";
	
	  }
}
