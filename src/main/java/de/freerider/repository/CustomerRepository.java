package de.freerider.repository;

import org.springframework.stereotype.Repository;
import de.freerider.datamodel.Customer;

@Repository
public interface CustomerRepository extends
        org.springframework.data.repository.CrudRepository<Customer, Long>
{

}
