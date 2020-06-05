package com.xbzn.Intercom.serial;

/**
 * Created by Enzo Cotter on 2020/6/5.
 */
public interface SerialConfig {
     String  OFF_GREEN = "echo 0 > /sys/devices/platform/Tricolor-led/leds/green-light/brightness";
     String  ON_GREEN = "echo 1 > /sys/devices/platform/Tricolor-led/leds/green-light/brightness";
     String  OFF_RED = "echo 0 > /sys/devices/platform/Tricolor-led/leds/red-light/brightness";
     String  ON_RED = "echo 1 > /sys/devices/platform/Tricolor-led/leds/red-light/brightness";
     String  OFF_WHITE = "echo 0 > /sys/devices/platform/Tricolor-led/leds/white-light/brightness";
     String  ON_WHITE = "echo 1 > /sys/devices/platform/Tricolor-led/leds/white-light/brightness";
}
