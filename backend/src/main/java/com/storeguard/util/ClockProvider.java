package com.storeguard.util;

import org.springframework.stereotype.Component;
import java.time.Clock;

@Component
public class ClockProvider {
    private final Clock clock = Clock.systemUTC();
    public Clock getClock() { return clock; }
}
