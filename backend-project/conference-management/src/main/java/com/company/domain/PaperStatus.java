package com.company.domain;

/**
 * Created by Alex on 21.05.2017.
 */

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

public enum PaperStatus {
    SUBMITTED,
    WAITING_FOR_BID,
    WAITING_FOR_REVIEW,
    ACCEPTED,
    DECLINED,
    CONTRADICTORY_REVIEW
}
