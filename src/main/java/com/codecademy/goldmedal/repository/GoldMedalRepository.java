package com.codecademy.goldmedal.repository;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

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

    // Filter gold medals by 1 field
    List<GoldMedal> findByCountry(String countryName);

    // Filter gold medals by season
    List<GoldMedal> findBySeasonOrderByYearAsc(String season);

    // Filter a gold medal list to distinct events
//    Set<GoldMedal> findDistinctByEvent(List<GoldMedal> goldMedalList);

    // Filter gold medal wins by gender
    List<GoldMedal> findByGender(String gender);
}
