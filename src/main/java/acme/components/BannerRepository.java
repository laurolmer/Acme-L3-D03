
package acme.components;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.banner.Banner;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface BannerRepository extends AbstractRepository {

	@Query("select count(b) from Banner b")
	int countBanners();

	@Query("select b from Banner b")
	List<Banner> findAllBanners(PageRequest pageRequest);

	default Banner randomiseBanners() {
		Banner result;
		int bannerCount, bannerIndex;
		ThreadLocalRandom random;
		PageRequest page;
		List<Banner> list;
		bannerCount = this.countBanners();
		if (bannerCount == 0)
			result = null;
		else {
			random = ThreadLocalRandom.current();
			bannerIndex = random.nextInt(0, bannerCount);
			page = PageRequest.of(bannerIndex, 1);
			list = this.findAllBanners(page);
			result = list.isEmpty() ? null : list.get(0);
		}
		return result;
	}
}
