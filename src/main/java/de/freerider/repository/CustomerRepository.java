package de.freerider.repository;

import org.springframework.stereotype.Repository;
import de.freerider.datamodel.Customer;

import javax.persistence.Entity;

@Repository
public interface CustomerRepository extends
        org.springframework.data.repository.CrudRepository<Customer, Long>
{

}
