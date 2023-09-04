package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Date date = java.sql.Date.valueOf(LocalDate.now());
        int noOfScreens = subscriptionEntryDto.getNoOfScreensRequired();
        SubscriptionType type = subscriptionEntryDto.getSubscriptionType();
        int totalAmount = calculateAmount(noOfScreens,type);
        Subscription subscription = new Subscription(type,
                noOfScreens,date,totalAmount);
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        subscription.setUser(user);
        Subscription subscription1 = subscriptionRepository.save(subscription);
        user.setSubscription(subscription1);
        userRepository.save(user);



        return totalAmount;
    }
    public int calculateAmount(int noOfScreens,SubscriptionType type){
        int price ;
        if(type.equals(SubscriptionType.BASIC)){
            price = 500 +(200*noOfScreens);
        } else if (type.equals(SubscriptionType.PRO)) {
            price = 800 +(250*noOfScreens);
        }
        else {
            price = 1000 +(300*noOfScreens);
        }
        return price;
    }
    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get();
        SubscriptionType type = user.getSubscription().getSubscriptionType();
        if(type.equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        } else if (type.equals(SubscriptionType.PRO)) {
            type = SubscriptionType.ELITE;

        }
        else {
            type = SubscriptionType.PRO;
        }
        int oldPrice = user.getSubscription().getTotalAmountPaid();
        int noOfScreens = user.getSubscription().getNoOfScreensSubscribed();
        int newPrice = calculateAmount(noOfScreens,type);
        user.getSubscription().setSubscriptionType(type);
        user.getSubscription().setTotalAmountPaid(newPrice);
        userRepository.save(user);
        return newPrice-oldPrice;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        int revenue = 0;
        for(Subscription subscription:subscriptionList){
            revenue+=subscription.getTotalAmountPaid();
        }

        return revenue;
    }

}
