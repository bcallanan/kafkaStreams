package com.bcallanan.kstreamproducer.domain;

public record AllOrdersCountPerStore(String locationId,
                                     Long orderCount,
                                     OrderType orderType) {
    public static record Address(String addressLine1,
                                 String addressLine2,
                                 String city,
                                 String state,
                                 String zip) {
    }
}