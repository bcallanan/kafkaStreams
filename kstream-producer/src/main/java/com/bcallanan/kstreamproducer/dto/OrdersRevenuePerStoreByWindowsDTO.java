package com.bcallanan.kstreamproducer.dto;

import java.time.LocalDateTime;

import com.bcallanan.kstreamproducer.domain.OrderType;
import com.bcallanan.kstreamproducer.domain.TotalRevenue;

public record OrdersRevenuePerStoreByWindowsDTO(String locationId,
                                                TotalRevenue totalRevenue,
                                                OrderType orderType,
                                                LocalDateTime startWindow,
                                                LocalDateTime endWindow) {
}
