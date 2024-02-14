package com.bcallanan.kstreamproducer.dto;

import java.time.LocalDateTime;

import com.bcallanan.kstreamproducer.domain.OrderType;

public record OrdersCountPerStoreByWindowsDTO(String locationId,
                                              Long orderCount,
                                              OrderType orderType,
                                              LocalDateTime startWindow,
                                              LocalDateTime endWindow) {
}
