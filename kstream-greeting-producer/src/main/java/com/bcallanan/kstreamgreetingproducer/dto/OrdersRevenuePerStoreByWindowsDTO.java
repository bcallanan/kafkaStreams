package com.bcallanan.kstreamgreetingproducer.dto;

import com.bcallanan.kstreamgreetingproducer.domain.OrderType;
import com.bcallanan.kstreamgreetingproducer.domain.TotalRevenue;

import java.time.LocalDateTime;

public record OrdersRevenuePerStoreByWindowsDTO(String locationId,
                                                TotalRevenue totalRevenue,
                                                OrderType orderType,
                                                LocalDateTime startWindow,
                                                LocalDateTime endWindow) {
}
