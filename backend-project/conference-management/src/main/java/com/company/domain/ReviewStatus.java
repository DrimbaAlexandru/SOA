package com.company.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 21.05.2017.
 */

public enum ReviewStatus {
    STRONG_ACCEPT,
    ACCEPT,
    WEAK_ACCEPT,
    BORDERLINE,
    WEAK_REJECT,
    REJECT,
    STRONG_REJECT
}
