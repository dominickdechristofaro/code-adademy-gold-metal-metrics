package com.codecademy.goldmedal.repository;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GoldMedalRepository extends CrudRepository<GoldMedal, Integer> {
    // Filter gold medals by country, sort by fields, ASC
    List<GoldMedal> findByCountryOrderByYearAsc(String countryName);
    List<GoldMedal> findByCountryOrderBySeasonAsc(String countryName);
    List<GoldMedal> findByCountryOrderByCityAsc(String countryName);
    List<GoldMedal> findByCountryOrderByNameAsc(String countryName);
    List<GoldMedal> findByCountryOrderByEventAsc(String countryName);

    // Filter gold medals by country, sort by fields, DESC
    List<GoldMedal> findByCountryOrderByYearDesc(String countryName);
    List<GoldMedal> findByCountryOrderBySeasonDesc(String countryName);
    List<GoldMedal> findByCountryOrderByCityDesc(String countryName);
    List<GoldMedal> findByCountryOrderByNameDesc(String countryName);
    List<GoldMedal> findByCountryOrderByEventDesc(String countryName);
}
