package de.noctivag.skyblock.items;

import java.util.ArrayList;
import java.util.List;

/**
 * AccessoryBag für Spieler (Talisman-Mechanik)
 */
public class AccessoryBag {
    private final List<Accessory> accessories = new ArrayList<>();
    public void add(Accessory acc) { accessories.add(acc); }
    public void remove(Accessory acc) { accessories.remove(acc); }
    public List<Accessory> getAll() { return accessories; }
    public void clear() { accessories.clear(); }
}
