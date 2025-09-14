import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

public class Park4U {
    enum VehicleType { MOTO, CARRO, CAMIONETA }

    static class Ticket {
        String plate;
        VehicleType type;
        int level;
        LocalDateTime entry;
        boolean lostTicket;
        double paid;
        int hoursCharged;
        Ticket(String plate, VehicleType type, int level, LocalDateTime entry) {
            this.plate = plate;
            this.type = type;
            this.level = level;
            this.entry = entry;
            this.lostTicket = false;
            this.paid = 0;
            this.hoursCharged = 0;
        }
    }

    static ArrayList<Ticket> active = new ArrayList<>();
    static ArrayList<Ticket> history = new ArrayList<>();
    static HashMap<String, Integer> levelCapacity = new HashMap<>();
    static HashMap<String, String> memberships = new HashMap<>();
    static HashMap<String, Double> plateTotals = new HashMap<>();
    static double subtotalMoto = 0;
    static double subtotalCarro = 0;
    static double subtotalCamioneta = 0;
    static int ticketsCount = 0;
    static double finesTotal = 0;
    static double discountsTotal = 0;
    static double surchargesTotal = 0;

    static final double PRICE_MOTO = 2500;
    static final double PRICE_CARRO = 4000;
    static final double PRICE_CAMIONETA = 5500;

    static final double MEMBERS_BASIC = 80000;
    static final double MEMBERS_PLUS = 120000;
    static final double MEMBERS_PREMIUM = 180000;

    public static void main(String[] args) {
        levelCapacity.put("1", 30);
        levelCapacity.put("2", 40);
        levelCapacity.put("3", 40);
        levelCapacity.put("4", 50);

        String opt;
        do {
            opt = JOptionPane.showInputDialog(null,
                    "Park4U - Main Menu\n" +
                    "1. Register Entry\n" +
                    "2. Register Exit and Charge\n" +
                    "3. Memberships\n" +
                    "4. Reports\n" +
                    "5. Cash\n" +
                    "6. Exit",
                    "Park4U", JOptionPane.QUESTION_MESSAGE);
            if (opt == null) break;
            switch (opt) {
                case "1": registerEntry(); break;
                case "2": registerExit(); break;
                case "3": membershipsMenu(); break;
                case "4": reportsMenu(); break;
                case "5": cashMenu(); break;
                case "6": break;
                default: JOptionPane.showMessageDialog(null, "Invalid option");
            }
        } while (!"6".equals(opt));
    }

    static String askValidated(String prompt, String regex) {
        int attempts = 0;
        while (attempts < 3) {
            String v = JOptionPane.showInputDialog(prompt);
            if (v == null) return null;
            if (v.matches(regex)) return v;
            attempts++;
            JOptionPane.showMessageDialog(null, "Invalid input. Attempts left: " + (3 - attempts));
        }
        JOptionPane.showMessageDialog(null, "Operation cancelled after 3 failed attempts.");
        return null;
    }

    static String askAny(String prompt) {
        int attempts = 0;
        while (attempts < 3) {
            String v = JOptionPane.showInputDialog(prompt);
            if (v == null) return null;
            if (!v.trim().isEmpty()) return v.trim();
            attempts++;
            JOptionPane.showMessageDialog(null, "Empty input. Attempts left: " + (3 - attempts));
        }
        JOptionPane.showMessageDialog(null, "Operation cancelled after 3 failed attempts.");
        return null;
    }

    static void registerEntry() {
        String plate = askAny("Enter plate (example ABC123):");
        if (plate == null) return;
        plate = plate.toUpperCase();

        String typeStr = askValidated("Enter type (MOTO, CARRO, CAMIONETA):", "(?i)MOTO|CARRO|CAMIONETA");
        if (typeStr == null) return;
        VehicleType type = VehicleType.valueOf(typeStr.toUpperCase());

        String levelStr = askValidated("Enter level (1-4):", "[1-4]");
        if (levelStr == null) return;
        int level = Integer.parseInt(levelStr);

        int used = countLevelOccupied(level);
        int cap = levelCapacity.get(String.valueOf(level));
        if (used >= cap) {
            JOptionPane.showMessageDialog(null, "Level full. Entry cancelled.");
            return;
        }

        String timeStr = askValidated("Enter entry time HH:mm (24h):", "([01]\\d|2[0-3]):[0-5]\\d");
        if (timeStr == null) return;
        LocalTime t = LocalTime.of(Integer.parseInt(timeStr.substring(0,2)), Integer.parseInt(timeStr.substring(3)));
        LocalDate today = LocalDate.now();
        LocalDateTime entry = LocalDateTime.of(today, t);

        Ticket ticket = new Ticket(plate, type, level, entry);
        active.add(ticket);
        JOptionPane.showMessageDialog(null, "Entry registered for " + plate);
    }

    static int countLevelOccupied(int level) {
        int c = 0;
        for (Ticket t : active) if (t.level == level) c++;
        return c;
    }

    static void registerExit() {
        if (active.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No active vehicles.");
            return;
        }
        String plate = askAny("Enter plate to exit:");
        if (plate == null) return;
        plate = plate.toUpperCase();
        Ticket found = null;
        for (Ticket t : active) if (t.plate.equals(plate)) { found = t; break; }
        if (found == null) { JOptionPane.showMessageDialog(null, "Plate not found."); return; }

        String lost = JOptionPane.showInputDialog("Is ticket lost? (yes/no)");
        if (lost == null) return;
        boolean lostFlag = lost.equalsIgnoreCase("yes") || lost.equalsIgnoreCase("y");
        found.lostTicket = lostFlag;

        if (memberships.containsKey(plate)) {
            found.paid = 0;
            found.hoursCharged = 0;
            finalizeExit(found);
            JOptionPane.showMessageDialog(null, "Monthly membership active. No charge.");
            return;
        }

        if (lostFlag) {
            double fine = 20000;
            double estTwoHours = calculateChargeByHours(found.type, 2);
            double total = fine + estTwoHours;
            found.paid = total;
            found.hoursCharged = 2;
            finesTotal += fine;
            ticketsCount++;
            addPlateTotal(found.plate, total);
            finalizeExit(found);
            JOptionPane.showMessageDialog(null, "Lost ticket. Fine applied. Total: " + format(total));
            return;
        }

        String timeStr = askValidated("Enter exit time HH:mm (24h):", "([01]\\d|2[0-3]):[0-5]\\d");
        if (timeStr == null) return;
        LocalTime t = LocalTime.of(Integer.parseInt(timeStr.substring(0,2)), Integer.parseInt(timeStr.substring(3)));
        LocalDate today = LocalDate.now();
        LocalDateTime exit = LocalDateTime.of(today, t);
        LocalDateTime entry = found.entry;
        if (exit.isBefore(entry)) exit = exit.plusDays(1);

        Duration dur = Duration.between(entry, exit);
        long totalMinutes = dur.toMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        if (minutes >= 15) hours = hours + 1;

        if (hours == 0) hours = 1;

        double baseCharge = calculateChargeByHours(found.type, (int)hours);

        boolean isNight = isNocturnal(entry, exit);
        boolean isWeekend = isWeekend(entry, exit);

        double surcharge = 0;
        if (isNight) surcharge += 0.15;
        if (isWeekend) surcharge += 0.10;

        double surchargeAmount = baseCharge * surcharge;

        double discountPercent = 0;
        String conv = checkCompanyAgreement(found.plate) ? "CONV" : null;
        boolean resident = checkResident(found.plate);
        boolean eco = checkEco(found.plate);
        if (conv != null) discountPercent = 0.12;
        if (resident && discountPercent < 0.10) discountPercent = 0.10;
        if (eco && discountPercent < 0.08) discountPercent = 0.08;

        double discountAmount = baseCharge * discountPercent;

        double total = baseCharge + surchargeAmount - discountAmount;
        if (total < 0) total = 0;

        found.paid = total;
        found.hoursCharged = (int)hours;

        if (found.type == VehicleType.MOTO) subtotalMoto += baseCharge;
        if (found.type == VehicleType.CARRO) subtotalCarro += baseCharge;
        if (found.type == VehicleType.CAMIONETA) subtotalCamioneta += baseCharge;
        ticketsCount++;
        finesTotal += 0;
        discountsTotal += discountAmount;
        surchargesTotal += surchargeAmount;
        addPlateTotal(found.plate, total);

        String receipt = "Receipt\nPlate: " + found.plate +
                "\nType: " + found.type +
                "\nHours: " + hours +
                "\nBase charge: " + format(baseCharge) +
                "\nSurcharges: " + format(surchargeAmount) +
                "\nDiscount: " + format(discountAmount) +
                "\nTotal: " + format(total);
        JOptionPane.showMessageDialog(null, receipt);
        finalizeExit(found);
    }

    static double calculateChargeByHours(VehicleType type, int hours) {
        double per = 0;
        if (type == VehicleType.MOTO) per = PRICE_MOTO;
        if (type == VehicleType.CARRO) per = PRICE_CARRO;
        if (type == VehicleType.CAMIONETA) per = PRICE_CAMIONETA;
        return per * hours;
    }

    static boolean isNocturnal(LocalDateTime entry, LocalDateTime exit) {
        for (LocalDateTime t = entry; !t.isAfter(exit); t = t.plusMinutes(30)) {
            int h = t.getHour();
            if (h >= 21 || h < 6) return true;
        }
        return false;
    }

    static boolean isWeekend(LocalDateTime entry, LocalDateTime exit) {
        for (LocalDateTime t = entry; !t.isAfter(exit); t = t.plusHours(6)) {
            DayOfWeek d = t.getDayOfWeek();
            if (d == DayOfWeek.SATURDAY || d == DayOfWeek.SUNDAY) return true;
        }
        return false;
    }

    static boolean checkCompanyAgreement(String plate) {
        return false;
    }
    static boolean checkResident(String plate) {
        return false;
    }
    static boolean checkEco(String plate) {
        return false;
    }

    static void addPlateTotal(String plate, double amount) {
        double prev = plateTotals.containsKey(plate) ? plateTotals.get(plate) : 0;
        plateTotals.put(plate, prev + amount);
    }

    static void finalizeExit(Ticket t) {
        active.remove(t);
        history.add(t);
    }

    static String format(double v) {
        long whole = (long)v;
        long cents = Math.round((v - whole) * 100);
        if (cents < 10) return whole + ".0" + cents;
        return whole + "." + cents;
    }

    static void membershipsMenu() {
        String opt = JOptionPane.showInputDialog("Memberships\n1. Add\n2. Query\n3. Cancel\n4. Back");
        if (opt == null) return;
        switch (opt) {
            case "1": addMembership(); break;
            case "2": queryMembership(); break;
            case "3": cancelMembership(); break;
            default: break;
        }
    }

    static void addMembership() {
        String plate = askAny("Enter plate for membership:");
        if (plate == null) return;
        plate = plate.toUpperCase();
        if (memberships.containsKey(plate)) { JOptionPane.showMessageDialog(null, "Already member"); return; }
        String plan = askValidated("Enter plan (BASICO, PLUS, PREMIUM):", "(?i)BASICO|PLUS|PREMIUM");
        if (plan == null) return;
        memberships.put(plate, plan.toUpperCase());
        JOptionPane.showMessageDialog(null, "Membership added");
    }

    static void queryMembership() {
        String plate = askAny("Enter plate to query:");
        if (plate == null) return;
        plate = plate.toUpperCase();
        String p = memberships.get(plate);
        if (p == null) JOptionPane.showMessageDialog(null, "No membership");
        else JOptionPane.showMessageDialog(null, "Plan: " + p);
    }

    static void cancelMembership() {
        String plate = askAny("Enter plate to cancel:");
        if (plate == null) return;
        plate = plate.toUpperCase();
        if (!memberships.containsKey(plate)) { JOptionPane.showMessageDialog(null, "No membership"); return; }
        memberships.remove(plate);
        JOptionPane.showMessageDialog(null, "Membership cancelled");
    }

    static void reportsMenu() {
        String opt = JOptionPane.showInputDialog("Reports\n1. Top 3 plates\n2. Occupation per level\n3. Average hours per type\n4. Back");
        if (opt == null) return;
        switch (opt) {
            case "1": reportTop3(); break;
            case "2": reportOccupation(); break;
            case "3": reportAverages(); break;
            default: break;
        }
    }

    static void reportTop3() {
        ArrayList<Map.Entry<String, Double>> list = new ArrayList<>(plateTotals.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> a, Map.Entry<String, Double> b) {
                return Double.compare(b.getValue(), a.getValue());
            }
        });
        String out = "Top 3 plates\n";
        for (int i = 0; i < 3 && i < list.size(); i++) {
            Map.Entry<String, Double> e = list.get(i);
            out += (i+1) + ". " + e.getKey() + " - " + format(e.getValue()) + "\n";
        }
        JOptionPane.showMessageDialog(null, out);
    }

    static void reportOccupation() {
        String out = "Occupation per level\n";
        for (Map.Entry<String, Integer> cap : levelCapacity.entrySet()) {
            int lvl = Integer.parseInt(cap.getKey());
            int used = countLevelOccupied(lvl);
            out += "Level " + cap.getKey() + ": " + used + " / " + cap.getValue() + "\n";
        }
        JOptionPane.showMessageDialog(null, out);
    }

    static void reportAverages() {
        double totalHoursMoto = 0; int countMoto = 0;
        double totalHoursCarro = 0; int countCarro = 0;
        double totalHoursCam = 0; int countCam = 0;
        for (Ticket t : history) {
            if (t.hoursCharged <= 0) continue;
            if (t.type == VehicleType.MOTO) { totalHoursMoto += t.hoursCharged; countMoto++; }
            if (t.type == VehicleType.CARRO) { totalHoursCarro += t.hoursCharged; countCarro++; }
            if (t.type == VehicleType.CAMIONETA) { totalHoursCam += t.hoursCharged; countCam++; }
        }
        String out = "Average hours per type\n";
        out += "MOTO: " + (countMoto==0 ? "0" : String.format("%.2f", totalHoursMoto / countMoto)) + "\n";
        out += "CARRO: " + (countCarro==0 ? "0" : String.format("%.2f", totalHoursCarro / countCarro)) + "\n";
        out += "CAMIONETA: " + (countCam==0 ? "0" : String.format("%.2f", totalHoursCam / countCam)) + "\n";
        JOptionPane.showMessageDialog(null, out);
    }

    static void cashMenu() {
        String out = "Cash summary\n" +
                "Subtotal MOTO: " + format(subtotalMoto) + "\n" +
                "Subtotal CARRO: " + format(subtotalCarro) + "\n" +
                "Subtotal CAMIONETA: " + format(subtotalCamioneta) + "\n" +
                "Tickets charged: " + ticketsCount + "\n" +
                "Fines total: " + format(finesTotal) + "\n" +
                "Discounts applied: " + format(discountsTotal) + "\n" +
                "Surcharges applied: " + format(surchargesTotal) + "\n" +
                "Total turn: " + format(subtotalMoto + subtotalCarro + subtotalCamioneta + finesTotal + surchargesTotal - discountsTotal);
        JOptionPane.showMessageDialog(null, out);
        String opt = JOptionPane.showInputDialog("1. Close shift and reset accumulators\n2. Back");
        if (opt == null) return;
        if ("1".equals(opt)) {
            subtotalMoto = 0; subtotalCarro = 0; subtotalCamioneta = 0;
            ticketsCount = 0; finesTotal = 0; discountsTotal = 0; surchargesTotal = 0;
            plateTotals.clear();
            history.clear();
            JOptionPane.showMessageDialog(null, "Shift closed and accumulators reset. Memberships preserved.");
        }
    }
}
