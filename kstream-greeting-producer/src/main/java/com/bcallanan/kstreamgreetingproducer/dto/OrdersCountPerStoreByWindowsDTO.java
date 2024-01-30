package com.bcallanan.kstreamgreetingproducer.dto;

import com.bcallanan.kstreamgreetingproducer.domain.OrderType;

import java.time.LocalDateTime;

public record OrdersCountPerStoreByWindowsDTO(String locationId,
                                              Long orderCount,
                                              OrderType orderType,
                                              LocalDateTime startWindow,
                                              LocalDateTime endWindow) {
}
