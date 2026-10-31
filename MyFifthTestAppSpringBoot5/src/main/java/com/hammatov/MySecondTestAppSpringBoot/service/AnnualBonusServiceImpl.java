package com.hammatov.MySecondTestAppSpringBoot.service;

import com.hammatov.MySecondTestAppSpringBoot.model.Positions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Year;

@Slf4j
@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {

    private static final int QUARTERS_IN_YEAR = 4;

    // Часы нужны, чтобы год можно было «подменить» в тестах и проверить и обычный, и високосный год
    private final Clock clock;

    // Конструктор для Spring: берёт текущую дату системы
    @Autowired
    public AnnualBonusServiceImpl() {
        this(Clock.systemDefaultZone());
    }

    // Конструктор для тестов: год определяется переданными часами
    public AnnualBonusServiceImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public double calculate(Positions pos, double salary, double bonus, int workDays) {
        if (workDays <= 0) {
            throw new IllegalArgumentException("Количество рабочих дней должно быть больше 0, получено: " + workDays);
        }

        // 366 дней в високосном году, 365 в остальных
        int daysInYear = Year.now(clock).length();
        double result = salary * bonus * daysInYear * pos.getPositionCoefficient() / workDays;

        log.info("AnnualBonusServiceImpl: годовая премия: должность {}, оклад {}, бонус {}, рабочих дней {}, дней в году {} -> {}",
                pos, salary, bonus, workDays, daysInYear, result);
        return result;
    }

    @Override
    public double calculateQuarterlyBonus(Positions pos, double salary, double bonus, int workDays) {
        if (!pos.isManager()) {
            throw new IllegalArgumentException("Квартальная премия рассчитывается только для руководителей, должность "
                    + pos + " к ним не относится");
        }

        double result = calculate(pos, salary, bonus, workDays) / QUARTERS_IN_YEAR;

        log.info("AnnualBonusServiceImpl: квартальная премия = годовая премия / {} = {}", QUARTERS_IN_YEAR, result);
        return result;
    }

}
