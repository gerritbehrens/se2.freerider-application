package de.freerider.repository;

import de.freerider.datamodel.Customer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

@Component
public class CustomerRepository implements CrudRepository<Customer, Long> {

    private HashMap<Long, Customer> customers = new HashMap<>();

    @Override
    public <S extends Customer> S save(S entity) {
        if (entity == null) {
            throw new IllegalArgumentException("The given entity is null.");
        } else {
            customers.put(entity.getId(), entity);
        }
        return entity;
    }

    @Override
    public <S extends Customer> Iterable<S> saveAll(Iterable<S> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("The iterable was null.");
        } else {
            Iterator<S> it = entities.iterator();
            if (it.hasNext()) {
                Customer customer = it.next();
                save(customer);
            }
        }
        return entities;
    }

    @Override
    public boolean existsById(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Object with this ID null does not exist.");
        } else {
            return customers.containsKey(aLong);
        }
    }

    @Override
    public Optional<Customer> findById(Long aLong) {
        Optional<Customer> customer = Optional.empty();
        if (aLong == null) {
            throw new IllegalArgumentException("Object with the ID null does not exist.");
        } else {
            if (existsById(aLong)) {
                customer = Optional.of(customers.get(aLong));
            }
        }
        return customer;
    }

    @Override
    public Iterable<Customer> findAll() {
        Iterable<Customer> c;
        if (customers == null) {
            throw new IllegalArgumentException("Customers was null.");
        } else {
            c = customers.values();
        }
        return c;
    }

    @Override
    public Iterable<Customer> findAllById(Iterable<Long> longs) {
        ArrayList<Customer> customerReturn = new ArrayList<>();
        if (longs == null) {
            throw new IllegalArgumentException("Longs was null.");
        } else {
            for (Long l : longs) {
                Optional<Customer> customer = findById(l);
                customer.ifPresent(customerReturn::add);
            }
        }
        return customerReturn;
    }

    @Override
    public long count() {
        return customers.size();
    }

    @Override
    public void deleteById(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Object with the ID null does not exist.");
        } else {
            if (existsById(aLong)) customers.remove(aLong);
        }
    }

    @Override
    public void delete(Customer entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        } else {
            customers.remove(entity.getId(), entity);
        }
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        if (longs == null) {
            throw new IllegalArgumentException("Longs was null.");
        } else {
            for (Long l : longs) {
                Optional<Customer> customer = findById(l);
                if (customer.isPresent()) {
                    customers.remove(l);
                }
            }
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Customer> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Entities is null");
        } else {
            for (Customer c : entities) {
                if (existsById(c.getId())) customers.remove(c.getId());
            }
        }
    }

    @Override
    public void deleteAll() {
        customers.clear();
    }
}
