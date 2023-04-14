
package acme.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.entities.banner.Banner;

@ControllerAdvice
public class BannerAdvisor {

	@Autowired
	private BannerRepository repository;


	@ModelAttribute("banner")
	public Banner getBanner() {
		Banner result;
		try {
			result = this.repository.randomiseBanners();
		} catch (final Throwable oops) {
			result = null;
		}
		return result;
	}
}
