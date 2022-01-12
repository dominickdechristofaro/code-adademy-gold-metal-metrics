package com.codecademy.goldmedal.repository;

import com.codecademy.goldmedal.model.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Integer> {

}
