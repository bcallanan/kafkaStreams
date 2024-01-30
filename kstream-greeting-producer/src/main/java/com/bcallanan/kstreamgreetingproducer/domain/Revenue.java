package com.bcallanan.kstreamgreetingproducer.domain;

import java.math.BigDecimal;
public record Revenue(String locationId,
                      BigDecimal finalAmount) {
}
