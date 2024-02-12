package com.bcallanan.kstreamgreetingproducer.domain;

import java.time.LocalDateTime;

public record Greeting(String message,
                    LocalDateTime timeStamp) {

}
