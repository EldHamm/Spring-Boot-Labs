package com.hammatov.MySecondTestAppSpringBoot.service;

import com.hammatov.MySecondTestAppSpringBoot.model.Positions;

public interface AnnualBonusService {

    /**
     * Годовая премия: salary * bonus * [дней в году] * коэффициент должности / workDays.
     * В високосном году дней в году 366, в остальных 365.
     */
    double calculate(Positions pos, double salary, double bonus, int workDays);

    /**
     * Квартальная премия (четверть годовой). Считается только для руководителей (isManager == true),
     * для остальных должностей выбрасывает IllegalArgumentException.
     */
    double calculateQuarterlyBonus(Positions pos, double salary, double bonus, int workDays);

}
