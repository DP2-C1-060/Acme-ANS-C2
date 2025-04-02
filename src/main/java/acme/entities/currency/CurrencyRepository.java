
package acme.entities.currency;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface CurrencyRepository extends AbstractRepository {

	@Query("select c.acceptedCurrency from CurrencyConfiguration c")
	String findAllAcceptedCurrency();
}
