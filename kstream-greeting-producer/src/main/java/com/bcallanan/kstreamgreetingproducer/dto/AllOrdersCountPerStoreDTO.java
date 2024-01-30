package com.bcallanan.kstreamgreetingproducer.dto;

import com.bcallanan.kstreamgreetingproducer.domain.OrderType;

public record AllOrdersCountPerStoreDTO(String locationId,
                                        Long orderCount,
                                        OrderType orderType) {
}
