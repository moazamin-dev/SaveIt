package com.saveit.service;

public class BudgetEvent {

    // ── Event types ───────────────────────────────────────────────────────────

    public enum Type {

        BUDGET_EXCEEDED,

        BUDGET_WARNING,

        BUDGET_OK
    }

    // ── Fields ────────────────────────────────────────────────────────────────

    private final Type   type;
    private final double amountSpent;
    private final double limit;

    // ── Constructor ───────────────────────────────────────────────────────────

    public BudgetEvent(Type type, double amountSpent, double limit) {
        this.type        = type;
        this.amountSpent = amountSpent;
        this.limit       = limit;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public Type   getType()        { return type;        }
    public double getAmountSpent() { return amountSpent; }
    public double getLimit()       { return limit;       }

    public double getRatio() {
        return limit > 0 ? amountSpent / limit : 0;
    }
}