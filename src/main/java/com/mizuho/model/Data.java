package com.mizuho.model;

import java.io.Serializable;

/**
 * Base for all domain classes to make sure these methods are implemented.
 */
public interface Data extends Serializable {

    boolean equals(Data data);
    int hashCode();

}
