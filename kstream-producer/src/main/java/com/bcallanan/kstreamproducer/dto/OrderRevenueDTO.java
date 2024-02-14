package com.bcallanan.kstreamproducer.dto;

import com.bcallanan.kstreamproducer.domain.OrderType;
import com.bcallanan.kstreamproducer.domain.TotalRevenue;

public record OrderRevenueDTO(
        String locationId,

        OrderType orderType,
        TotalRevenue totalRevenue
) {
}
