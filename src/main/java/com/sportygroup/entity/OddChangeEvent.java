package com.sportygroup.entity;

import com.sportygroup.entity.enums.BetOutcome;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class OddChangeEvent extends Event {

    private Map<BetOutcome, Double> odds;
}
