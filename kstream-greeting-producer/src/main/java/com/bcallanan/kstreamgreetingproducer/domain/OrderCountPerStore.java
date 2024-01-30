package com.bcallanan.kstreamgreetingproducer.domain;

public record OrderCountPerStore(String locationId,
                                 Long orderCount) {
}
