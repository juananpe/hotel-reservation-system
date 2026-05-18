# Modelling Notes

## `DailyPrice`
Captures the rule *"prices depend on hotel and dates"* and *"all rooms of the same type in a hotel have the same price on the same day"* — it's keyed by `(Hotel, RoomType, Date)`, not by individual `Room`.

## `AdditionalDayPrice`
Separate from `DailyPrice` because the statement says it's set **per room type** and **differs per hotel** (so it's **not date-dependent** like `DailyPrice`).

## `Reservation` → `Room` assignment
A `Reservation` is initially made for a `RoomType` (not a specific `Room`); a concrete `Room` is only assigned at check-in — hence the `0..1` multiplicity on the `Room` link.

## `User` vs `RegisteredUser`
Unregistered users can query but **not** reserve, so only the subclass owns reservations, cards, and waiting-list entries.