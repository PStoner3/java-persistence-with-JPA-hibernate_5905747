package com.mycompany.app.entities.keys;

import java.io.Serializable;

public class BookTypeKey implements Serializable {
  private String code;
  private String subCode;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getSubCode() {
    return subCode;
  }

  public void setSubCode(String subCode) {
    this.subCode = subCode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((subCode == null) ? 0 : subCode.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BookTypeKey other = (BookTypeKey) obj;
    if (code == null) {
      if (other.code != null)
        return false;
    } else if (!code.equals(other.code))
      return false;
    if (subCode == null) {
      if (other.subCode != null)
        return false;
    } else if (!subCode.equals(other.subCode))
      return false;
    return true;
  }
}
