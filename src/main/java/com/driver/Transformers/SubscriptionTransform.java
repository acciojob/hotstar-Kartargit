package com.driver.Transformers;
import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;

import javax.xml.crypto.Data;
import java.util.Date;
public class SubscriptionTransform {
    public static Subscription convertDtoToEntity(SubscriptionEntryDto entryDto){
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(entryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(entryDto.getNoOfScreensRequired());

        return subscription;
    }
}
