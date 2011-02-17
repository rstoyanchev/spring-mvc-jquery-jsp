package org.springframework.samples.travel;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HotelSearchController {

	private BookingService bookingService;

	@Inject
	public HotelSearchController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@RequestMapping(value = "/hotels/main", method = RequestMethod.GET)
	public void main(SearchCriteria searchCriteria) {
	}

	@RequestMapping(value = "/hotels/search", method = RequestMethod.GET)
	public void search(SearchCriteria searchCriteria) {
	}

	@RequestMapping(value = "/hotels", method = RequestMethod.GET)
	public String list(SearchCriteria criteria, Model model) {
		List<Hotel> hotels = bookingService.findHotels(criteria);
		model.addAttribute(hotels);
		return "hotels/list";
	}

}