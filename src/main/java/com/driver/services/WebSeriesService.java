package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        Optional<WebSeries> webSeriesOp = Optional.ofNullable(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()));

        if(webSeriesOp.isPresent())throw  new Exception("Series is already present");

        WebSeries webSeries = new WebSeries(webSeriesEntryDto.getSeriesName(),webSeriesEntryDto.getAgeLimit(),
                webSeriesEntryDto.getRating(),webSeriesEntryDto.getSubscriptionType());

        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();
        List<WebSeries> webSeriesList = productionHouse.getWebSeriesList();
        webSeriesList.add(webSeries);
        double ratingSum = 0;
        for(WebSeries series:webSeriesList){
            ratingSum+=series.getRating();
        }
        double newRating = ratingSum/webSeriesList.size();
        productionHouse.setRatings(newRating);
        webSeries.setProductionHouse(productionHouse);
        WebSeries webSeries1 = webSeriesRepository.save(webSeries);
        productionHouseRepository.save(productionHouse);

        return webSeries1.getId();
    }

}
