package com.hammatov.MySecondTestAppSpringBoot.service;

import com.hammatov.MySecondTestAppSpringBoot.model.Positions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class AnnualBonusServiceImplTest {

    // Допустимая погрешность при сравнении чисел с плавающей точкой
    private static final double OFFSET = 0.0001;

    // Общие исходные данные: оклад 100000, коэффициент премии 2.0, 243 рабочих дня
    private static final double SALARY = 100000.00;
    private static final double BONUS = 2.0;
    private static final int WORK_DAYS = 243;

    // Сервис, который «думает», что сейчас указанный год (15 июня)
    private static AnnualBonusServiceImpl serviceForYear(int year) {
        Clock clock = Clock.fixed(Instant.parse(year + "-06-15T12:00:00Z"), ZoneOffset.UTC);
        return new AnnualBonusServiceImpl(clock);
    }

    // ---------- годовая премия ----------

    @Test
    @DisplayName("Годовая премия в обычном году: используется 365 дней")
    void calculate_ordinaryYear_uses365Days() {
        // given
        AnnualBonusServiceImpl service = serviceForYear(2023);

        // when
        double result = service.calculate(Positions.HR, SALARY, BONUS, WORK_DAYS);

        // then
        assertThat(result).isCloseTo(360493.8271604938, within(OFFSET));
    }

    @Test
    @DisplayName("Годовая премия в високосном году: используется 366 дней")
    void calculate_leapYear_uses366Days() {
        // given
        AnnualBonusServiceImpl service = serviceForYear(2024);

        // when
        double result = service.calculate(Positions.HR, SALARY, BONUS, WORK_DAYS);

        // then
        assertThat(result).isCloseTo(361481.48148148146, within(OFFSET));
    }

    @ParameterizedTest
    @EnumSource(Positions.class)
    @DisplayName("Годовая премия для каждой должности использует её коэффициент")
    void calculate_everyPosition_usesItsCoefficient(Positions position) {
        // given
        AnnualBonusServiceImpl service = serviceForYear(2023);

        // when
        double result = service.calculate(position, SALARY, BONUS, WORK_DAYS);

        // then
        double expected = SALARY * BONUS * 365 * position.getPositionCoefficient() / WORK_DAYS;
        assertThat(result).isCloseTo(expected, within(OFFSET));
    }

    @Test
    @DisplayName("Годовая премия: 0 рабочих дней недопустимо")
    void calculate_zeroWorkDays_throwsException() {
        AnnualBonusServiceImpl service = serviceForYear(2023);

        assertThatThrownBy(() -> service.calculate(Positions.DEV, SALARY, BONUS, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("рабочих дней");
    }

    // ---------- квартальная премия ----------

    @Test
    @DisplayName("Квартальная премия руководителя (TL) в обычном году: четверть годовой, 365 дней")
    void calculateQuarterlyBonus_manager_ordinaryYear() {
        // given
        AnnualBonusServiceImpl service = serviceForYear(2023);

        // when
        double result = service.calculateQuarterlyBonus(Positions.TL, SALARY, BONUS, WORK_DAYS);

        // then
        assertThat(result).isCloseTo(195267.48971193415, within(OFFSET));
    }

    @Test
    @DisplayName("Квартальная премия руководителя (PO) в високосном году: четверть годовой, 366 дней")
    void calculateQuarterlyBonus_manager_leapYear() {
        // given
        AnnualBonusServiceImpl service = serviceForYear(2024);

        // when
        double result = service.calculateQuarterlyBonus(Positions.PO, SALARY, BONUS, WORK_DAYS);

        // then
        assertThat(result).isCloseTo(210864.19753086418, within(OFFSET));
    }

    @ParameterizedTest
    @EnumSource(value = Positions.class, names = {"TL", "PO", "TPM", "CTO"})
    @DisplayName("Квартальная премия всех руководителей равна четверти годовой")
    void calculateQuarterlyBonus_everyManager_isQuarterOfAnnual(Positions position) {
        // given
        AnnualBonusServiceImpl service = serviceForYear(2023);

        // when
        double quarterly = service.calculateQuarterlyBonus(position, SALARY, BONUS, WORK_DAYS);

        // then
        double annual = service.calculate(position, SALARY, BONUS, WORK_DAYS);
        assertThat(quarterly).isCloseTo(annual / 4, within(OFFSET));
    }

    @ParameterizedTest
    @EnumSource(value = Positions.class, names = {"DEV", "HR"})
    @DisplayName("Квартальная премия не рассчитывается для не руководителей")
    void calculateQuarterlyBonus_notManager_throwsException(Positions position) {
        AnnualBonusServiceImpl service = serviceForYear(2023);

        assertThatThrownBy(() -> service.calculateQuarterlyBonus(position, SALARY, BONUS, WORK_DAYS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("только для руководителей");
    }

}
