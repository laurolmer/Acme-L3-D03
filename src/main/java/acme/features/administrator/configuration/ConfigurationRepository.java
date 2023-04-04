
package acme.features.administrator.configuration;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.configuration.Configuration;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface ConfigurationRepository extends AbstractRepository {

	@Query("select c from Configuration c")
	Configuration findSystemConfiguration();

}
