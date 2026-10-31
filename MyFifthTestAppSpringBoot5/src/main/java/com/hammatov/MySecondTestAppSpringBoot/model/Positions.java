package com.hammatov.MySecondTestAppSpringBoot.model;

// Должности сотрудников: коэффициент для расчёта премии и признак руководителя
public enum Positions {

    /** Developer, разработчик */
    DEV(2.2, false),
    /** HR-specialist, специалист по кадрам */
    HR(1.2, false),
    /** Team Lead, руководитель команды */
    TL(2.6, true),
    /** Product Owner, владелец продукта */
    PO(2.8, true),
    /** Technical Product Manager, технический продакт-менеджер */
    TPM(2.5, true),
    /** Chief Technical Officer, технический директор */
    CTO(3.0, true);

    private final double positionCoefficient;
    private final boolean isManager;

    Positions(double positionCoefficient, boolean isManager) {
        this.positionCoefficient = positionCoefficient;
        this.isManager = isManager;
    }

    public double getPositionCoefficient() {
        return positionCoefficient;
    }

    // true для руководителей и иных управленцев (TL, PO, TPM, CTO)
    public boolean isManager() {
        return isManager;
    }

}
