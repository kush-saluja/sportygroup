package com.sportygroup.entity;

import com.sportygroup.entity.enums.BetOutcome;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class BetSettlementEvent extends Event {

    private BetOutcome outcome;
}
