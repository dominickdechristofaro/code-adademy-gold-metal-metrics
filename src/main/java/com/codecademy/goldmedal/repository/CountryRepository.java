package com.codecademy.goldmedal.repository;

import com.codecademy.goldmedal.model.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Integer> {
    Optional<Country> findByName(String name);

    // Find Country By Field, Order ASC
    List<Country> findAllByOrderByNameAsc();
    List<Country> findAllByOrderByGdpAsc();
    List<Country> findAllByOrderByPopulationAsc();

    // Find Country By Field, Order DESC
    List<Country> findAllByOrderByNameDesc();
    List<Country> findAllByOrderByGdpDesc();
    List<Country> findAllByOrderByPopulationDesc();

    // TODO: list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
}
