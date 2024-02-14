package com.bcallanan.kstreamproducer.domain;

public record OrderCountPerStore(String locationId,
                                 Long orderCount) {
}
