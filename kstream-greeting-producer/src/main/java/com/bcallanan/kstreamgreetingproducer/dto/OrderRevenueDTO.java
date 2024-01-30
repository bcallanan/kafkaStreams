package com.bcallanan.kstreamgreetingproducer.dto;

import com.bcallanan.kstreamgreetingproducer.domain.OrderType;
import com.bcallanan.kstreamgreetingproducer.domain.TotalRevenue;

public record OrderRevenueDTO(
        String locationId,

        OrderType orderType,
        TotalRevenue totalRevenue
) {
}
