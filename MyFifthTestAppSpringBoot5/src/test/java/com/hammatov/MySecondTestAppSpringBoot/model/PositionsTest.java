package com.hammatov.MySecondTestAppSpringBoot.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PositionsTest {

    @Test
    @DisplayName("Коэффициенты должностей соответствуют заданию")
    void positionCoefficients() {
        assertThat(Positions.DEV.getPositionCoefficient()).isEqualTo(2.2);
        assertThat(Positions.HR.getPositionCoefficient()).isEqualTo(1.2);
        assertThat(Positions.TL.getPositionCoefficient()).isEqualTo(2.6);
        assertThat(Positions.PO.getPositionCoefficient()).isEqualTo(2.8);
        assertThat(Positions.TPM.getPositionCoefficient()).isEqualTo(2.5);
        assertThat(Positions.CTO.getPositionCoefficient()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("Руководителями считаются только TL, PO, TPM и CTO")
    void managers() {
        assertThat(Positions.values())
                .filteredOn(Positions::isManager)
                .containsExactly(Positions.TL, Positions.PO, Positions.TPM, Positions.CTO);
    }

}
