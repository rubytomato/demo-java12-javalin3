package com.example.demo.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Prefecture {
  UNKNOWN(Byte.valueOf("0")),
  HOKKAIDO(Byte.valueOf("1")),
  TOHOKU(Byte.valueOf("2")),
  KANTOU(Byte.valueOf("3")),
  CHUBU(Byte.valueOf("4")),
  KINKI(Byte.valueOf("5")),
  CHUGOKU(Byte.valueOf("6")),
  SHIKOKU(Byte.valueOf("7")),
  KYUSYU_OKINAWA(Byte.valueOf("8"));

  private static final Map<Byte, Prefecture> LOOKUP;

  static {
    Map<Byte, Prefecture> temp = new HashMap<>();
    for (Prefecture prefecture : Prefecture.values()) {
      temp.put(prefecture.id, prefecture);
    }
    LOOKUP = Collections.unmodifiableMap(temp);
  }

  public static Prefecture lookupById(Byte id) {
    return LOOKUP.get(id);
  }

  private Byte id;

  Prefecture(Byte id) {
    this.id = id;
  }

  public Byte getId() {
    return id;
  }

}
