package com.anseong.authorization;

import java.io.Serializable;

public abstract class AbstractBaseClaims implements Claimable, Serializable {

     private static final long serialVersionUID = 1L;

     public abstract boolean equals(Object obj);

     public abstract int hashCode();

     public abstract String toString();
}
