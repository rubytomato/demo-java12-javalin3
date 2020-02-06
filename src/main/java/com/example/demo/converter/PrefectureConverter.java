package com.example.demo.converter;

import org.jooq.Converter;

public class PrefectureConverter implements Converter<Byte, Prefecture> {
  private static final long serialVersionUID = -4215853134618420514L;

  @Override
  public Prefecture from(Byte id) {
    return Prefecture.lookupById(id);
  }

  @Override
  public Byte to(Prefecture prefecture) {
    return prefecture.getId();
  }

  @Override
  public Class<Byte> fromType() {
    return Byte.class;
  }

  @Override
  public Class<Prefecture> toType() {
    return Prefecture.class;
  }

}
