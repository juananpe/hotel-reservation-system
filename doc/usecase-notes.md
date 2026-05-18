# Use Case Notes

## `Registered User` generalises `Unregistered User`
Anything a guest can do, a registered user can do too.

## `Make reservation` `<<include>>`s `Pay 20% advance`
The statement says *"para formalizar la reserva deberá realizar un pago adelantado del 20%"* — it's **mandatory**, not optional.

## `Join waiting list` `<<extend>>`s `Make reservation`
For the case where no rooms of that type are available (*"si lo desea"* — the user **may choose** to do it).

## `Charge late-checkout fee` `<<extend>>`s `Perform check-out`
Only fires past the **13:00** cutoff.

## `Banking System` as secondary actor
The `Banking System` is modelled as a **secondary actor** because every payment use case reaches out to it.