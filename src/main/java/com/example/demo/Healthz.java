package com.example.demo;

import lombok.Data;

@Data
class Healthz {

  private String status;

  Healthz() {}

  Healthz(String status) {
    this.status = status;
  }
}