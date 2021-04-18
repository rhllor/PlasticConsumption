package com.github.rhllor.pc.library;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.github.rhllor.pc.library.entity.Consumption;

import org.springframework.data.jpa.domain.Specification;

public class ConsumptionSpecification implements Specification<Consumption> {

    private static final long serialVersionUID = 1L;
    private SearchCriteria _criteria;

    public ConsumptionSpecification(SearchCriteria criteria) {
        this._criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Consumption> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        int yearAndWeekNumber;
        Expression<Integer> exp1;

        switch (this._criteria.getTypeSearch()) {

            case greaterOrEqual:
                yearAndWeekNumber = GetYearAndWeekNumber((Date)this._criteria.getValue());
                exp1 = MergeYearNumberColumn(root, builder);
                return builder.greaterThanOrEqualTo(exp1, yearAndWeekNumber);

            case lessOrEqual:
                yearAndWeekNumber = GetYearAndWeekNumber((Date)this._criteria.getValue());
                exp1 = MergeYearNumberColumn(root, builder);
                return builder.lessThanOrEqualTo(exp1.as(Integer.class), yearAndWeekNumber);

            default:
                return builder.equal(root.get(_criteria.getKey()), _criteria.getValue());
        }
    }

    private int GetYearAndWeekNumber(Date date) {
        int year =  GetDateValue((Date)this._criteria.getValue(), Calendar.YEAR);
        int weekNumber = GetDateValue((Date)this._criteria.getValue(), Calendar.WEEK_OF_YEAR);
        String s = String.format("%04d", year) + String.format("%03d", weekNumber);
        return Integer.parseInt(s);
    }

    private int GetDateValue(Date date, int type) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(type);
    }

    private Expression<Integer> MergeYearNumberColumn(Root<Consumption> root, CriteriaBuilder builder) {

        Expression<String> stringYear = SetPadding(
            root.get("year").as(String.class), builder, 4
        );

        Expression<String> stringWeek = SetPadding(
            root.get("weekNumber").as(String.class), builder, 3
        );

        return builder.concat(stringYear, stringWeek).as(Integer.class);
    }

    private Expression<String> SetPadding(Expression<String> column, CriteriaBuilder builder, int length) {

        Expression<Integer> lengthExp = builder.literal(length);
        Expression<String> fillText = builder.literal("0");

        return builder.function(
            "LPAD",
            String.class,
            column, lengthExp, fillText);
    }

}