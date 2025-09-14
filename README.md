Park4U - README

Run:
javac Park4U.java
java Park4U

How it works:
- Use the main menu to register entries, exits, memberships, reports and cash.
- All input uses JOptionPane dialogs.
- Each field validation allows up to 3 attempts. After 3 failed attempts the operation cancels.
- Time format must be HH:mm (24-hour).
- Vehicle types: MOTO, CARRO, CAMIONETA.
- Levels: 1 to 4 with fixed capacities.
- Memberships: BASICO, PLUS, PREMIUM. Memberships are registered by plate.
- If a plate has an active membership, parking for exits is free.
- Lost ticket: fixed fine 20000 + estimated 2 hours minimum.
- Rounding rule: fractions >= 15 minutes round up to next whole hour.
- Night surcharge: +15% if any part of the stay is between 21:00 and 06:00.
- Weekend surcharge: +10% if any part of the stay is on Sat or Sun.
- Discounts: apply only the single largest (Company 12%, Resident 10%, Eco 8%).
- Cash menu shows totals per type, number of tickets, fines, discounts, surcharges, total.
- Closing shift resets accumulators but keeps memberships.

Notes and test cases:
1) CARRO entry 10:10 exit 12:05 => 1h55 => 2 hours => 2 * 4000 = 8000
2) MOTO 14:00 -> 15:10 => 1 hour (10 min < 15) => 1 * 2500
3) MOTO 14:00 -> 15:20 => 2 hours (20 min >= 15)
4) Lost ticket: fine 20000 + 2 hours estimated
5) Membership plate: no hourly charge
6) Reports show top 3 plates by revenue, occupancy per level, and average hours per type

Design decisions:
- All data kept in memory for the session.
- No try/catch used. Inputs validated with regex and max 3 attempts.
- Simple Ticket class to hold info.
- Membership checks are basic (membership map). Company/resident/eco checks are placeholders for additional logic and return false by default.

