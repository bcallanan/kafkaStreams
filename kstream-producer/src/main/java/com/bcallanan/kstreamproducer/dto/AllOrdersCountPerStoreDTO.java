package com.bcallanan.kstreamproducer.dto;

import com.bcallanan.kstreamproducer.domain.OrderType;

public record AllOrdersCountPerStoreDTO(String locationId,
                                        Long orderCount,
                                        OrderType orderType) {
}
