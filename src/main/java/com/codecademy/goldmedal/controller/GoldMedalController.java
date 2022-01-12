package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import com.codecademy.goldmedal.repository.CountryRepository;
import com.codecademy.goldmedal.repository.GoldMedalRepository;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    // Dependency Injected Variables
    private final CountryRepository countryRepository;
    private final GoldMedalRepository goldMedalRepository;

    // Constructor for autowiring repositories
    public GoldMedalController(CountryRepository countryRepository, GoldMedalRepository goldMedalRepository) {
        this.countryRepository = countryRepository;
        this.goldMedalRepository = goldMedalRepository;
    }

    // MVC Controllers
    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.equalsIgnoreCase("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.equalsIgnoreCase("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    // Helper Methods
    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryOrderByYearAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryOrderByYearDesc(countryName);
                }
                break;
            case "season":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryOrderBySeasonAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryOrderBySeasonDesc(countryName);
                }
                break;
            case "city":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryOrderByCityAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryOrderByCityDesc(countryName);
                }
                break;
            case "name":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryOrderByNameAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryOrderByNameDesc(countryName);
                }
                break;
            case "event":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryOrderByEventAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryOrderByEventDesc(countryName);
                }
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        var countryOptional = this.countryRepository.findByName(countryName);
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        var country = countryOptional.get();
        var goldMedalCount = this.goldMedalRepository.findByCountry(countryName).size();

        var summerWins = this.goldMedalRepository.findBySeasonOrderByYearAsc("Summer");
        var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
//        var totalSummerEvents = this.goldMedalRepository.findDistinctByEvent(summerWins).size();
//        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        var winterWins = this.goldMedalRepository.findBySeasonOrderByYearAsc("Winter");
        var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
//        var totalWinterEvents = this.goldMedalRepository.findDistinctByEvent(winterWins).size();
//        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

        var numberEventsWonByFemaleAthletes = this.goldMedalRepository.findByGender("Women").size();
        var numberEventsWonByMaleAthletes = this.goldMedalRepository.findByGender("Men").size();

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                50.00f,
                yearFirstSummerWin,
                numberWinterWins,
                50.0f,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        switch (sortBy) {
            case "name":
                if (ascendingOrder) {
                    countries = this.countryRepository.findAllByOrderByNameAsc();
                } else {
                    countries = this.countryRepository.findAllByOrderByNameDesc();
                }
                break;
            case "gdp":
                if (ascendingOrder) {
                    countries = this.countryRepository.findAllByOrderByGdpAsc();
                } else {
                    countries = this.countryRepository.findAllByOrderByGdpDesc();
                }
                break;
            case "population":
                if (ascendingOrder) {
                    countries = this.countryRepository.findAllByOrderByPopulationAsc();
                } else {
                    countries = this.countryRepository.findAllByOrderByPopulationDesc();
                }
                break;
            case "medals":
            default:
                countries = this.countryRepository.findAllByOrderByNameAsc();
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = this.goldMedalRepository.findByCountry(country.getName()).size();
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
