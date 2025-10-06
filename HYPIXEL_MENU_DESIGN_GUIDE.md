# 🎨 HYPIXEL MENU DESIGN GUIDE
## Complete Visual Design System for Hypixel Skyblock Recreation

---

## 📋 **OVERVIEW**

This document defines the complete visual design system for the Hypixel Skyblock Recreation project, ensuring consistency across all GUI elements and maintaining the authentic Hypixel Skyblock aesthetic.

---

## 🎨 **COLOR PALETTE**

### **Primary Colors**
- **Green (`&a`):** `#55FF55` - Success, positive actions, common items
- **Red (`&c`):** `#FF5555` - Errors, negative actions, mythic items
- **Yellow (`&e`):** `#FFFF55` - Warnings, special items, navigation
- **Blue (`&9`):** `#5555FF` - Information, rare items, links
- **Purple (`&5`):`#AA00AA` - Epic items, special features
- **Gold (`&6`):** `#FFAA00` - Legendary items, premium features
- **Gray (`&7`):** `#AAAAAA` - Secondary text, descriptions
- **Dark Gray (`&8`):** `#555555` - Tertiary text, disabled elements
- **White (`&f`):** `#FFFFFF` - Primary text, common items
- **Black (`&0`):** `#000000` - Borders, contrast elements

### **Hex Color Support**
- **Format:** `&#RRGGBB` (e.g., `&#FF6B6B` for custom colors)
- **Usage:** For special effects and custom branding
- **Examples:**
  - `&#FF6B6B` - Coral red
  - `&#4ECDC4` - Turquoise
  - `&#45B7D1` - Sky blue
  - `&#96CEB4` - Mint green

---

## 🏗️ **LAYOUT SYSTEM**

### **Inventory Sizes**
- **Small (27 slots):** 3x9 - Simple menus, single-page content
- **Medium (36 slots):** 4x9 - Standard menus, moderate content
- **Large (45 slots):** 5x9 - Complex menus, extensive content
- **Extra Large (54 slots):** 6x9 - Main menus, comprehensive content

### **Grid System**
- **Border:** Always use black stained glass panes (`BLACK_STAINED_GLASS_PANE`)
- **Filler:** Gray stained glass panes (`GRAY_STAINED_GLASS_PANE`) for empty slots
- **Content Area:** Center area for interactive elements
- **Navigation:** Bottom row for navigation buttons

### **Slot Positioning**
```
┌─────────────────────────────────────┐
│ 0  1  2  3  4  5  6  7  8          │ Top Border
│ 9  10 11 12 13 14 15 16 17         │
│ 18 19 20 21 22 23 24 25 26         │ Content Area
│ 27 28 29 30 31 32 33 34 35         │
│ 36 37 38 39 40 41 42 43 44         │
│ 45 46 47 48 49 50 51 52 53         │ Bottom Border
└─────────────────────────────────────┘
```

---

## 🎯 **ITEM RARITY SYSTEM**

### **Common Items**
- **Color:** White (`&f`)
- **Format:** `&fItem Name`
- **Usage:** Basic items, standard equipment
- **Example:** `&fWooden Sword`

### **Uncommon Items**
- **Color:** Green (`&a`)
- **Format:** `&aItem Name`
- **Usage:** Improved items, basic upgrades
- **Example:** `&aIron Sword`

### **Rare Items**
- **Color:** Blue (`&9`)
- **Format:** `&9Item Name`
- **Usage:** Advanced items, significant upgrades
- **Example:** `&9Diamond Sword`

### **Epic Items**
- **Color:** Purple (`&5`)
- **Format:** `&5Item Name`
- **Usage:** High-tier items, major upgrades
- **Example:** `&5Netherite Sword`

### **Legendary Items**
- **Color:** Gold (`&6`)
- **Format:** `&6Item Name`
- **Usage:** Top-tier items, premium upgrades
- **Example:** `&6Aspect of the End`

### **Mythic Items**
- **Color:** Red (`&c`)
- **Format:** `&cItem Name`
- **Usage:** Ultimate items, maximum upgrades
- **Example:** `&cHyperion`

### **Special Items**
- **Color:** Yellow (`&e`)
- **Format:** `&eItem Name`
- **Usage:** Unique items, special effects
- **Example:** `&eBooster Cookie`

---

## 📝 **TEXT FORMATTING**

### **Item Names**
- **Format:** `[Rarity Color][Item Name]`
- **Examples:**
  - `&aSkills` - Green for common features
  - `&6Auction House` - Gold for premium features
  - `&cDungeon Finder` - Red for combat features

### **Lore Formatting**
- **Primary Description:** `&7[Description]`
- **Secondary Information:** `&8[Additional Info]`
- **Action Text:** `&e[Action]`
- **Empty Lines:** `""` for spacing

### **Lore Examples**
```java
lore(
    "&7Verbessere deine Fähigkeiten",
    "&7und sammle XP in verschiedenen",
    "&7Bereichen wie Bergbau, Kampf, etc.",
    "",
    "&eKlicke zum Öffnen"
)
```

---

## 🎮 **GUI COMPONENTS**

### **Navigation Buttons**
- **Back Button:**
  - Material: `ARROW`
  - Name: `&eZurück`
  - Lore: `&7Klicke zum Zurückgehen`

- **Next Button:**
  - Material: `ARROW`
  - Name: `&eWeiter`
  - Lore: `&7Klicke zum Weitergehen`

- **Close Button:**
  - Material: `BARRIER`
  - Name: `&cSchließen`
  - Lore: `&7Klicke zum Schließen`

### **Action Buttons**
- **Confirm Button:**
  - Material: `LIME_STAINED_GLASS_PANE`
  - Name: `&aBestätigen`
  - Lore: `&7Klicke zum Bestätigen`

- **Cancel Button:**
  - Material: `RED_STAINED_GLASS_PANE`
  - Name: `&cAbbrechen`
  - Lore: `&7Klicke zum Abbrechen`

### **Status Indicators**
- **Enabled:**
  - Material: `LIME_DYE`
  - Name: `&aAktiviert`
  - Lore: `&7Klicke zum Deaktivieren`

- **Disabled:**
  - Material: `GRAY_DYE`
  - Name: `&7Deaktiviert`
  - Lore: `&7Klicke zum Aktivieren`

---

## 🎨 **VISUAL EFFECTS**

### **Enchantment Glow**
- **Usage:** For special items, premium features
- **Implementation:** Add `DURABILITY` enchantment with `HIDE_ENCHANTS` flag
- **Examples:** Legendary items, special buttons

### **Custom Model Data**
- **Usage:** For unique item appearances
- **Implementation:** Set `customModelData` on ItemMeta
- **Examples:** Custom icons, special textures

### **Particle Effects**
- **Usage:** For interactive elements, special actions
- **Implementation:** Spawn particles on click
- **Examples:** Button clicks, item interactions

---

## 📱 **RESPONSIVE DESIGN**

### **Screen Size Adaptation**
- **Small Screens:** Use 27-slot inventories
- **Medium Screens:** Use 36-slot inventories
- **Large Screens:** Use 45-slot inventories
- **Extra Large Screens:** Use 54-slot inventories

### **Content Prioritization**
- **Primary Actions:** Center slots (11-17, 20-26, 29-35)
- **Secondary Actions:** Side slots (10, 12, 14, 16, 18, 19, 21, 23, 25, 27, 28, 30, 32, 34, 36, 37, 39, 41)
- **Navigation:** Bottom row (45-53)
- **Borders:** Always maintain border consistency

---

## 🎯 **INTERACTION PATTERNS**

### **Click Feedback**
- **Sound Effects:** Use appropriate click sounds
- **Visual Feedback:** Brief item highlight
- **Text Feedback:** Action confirmation messages

### **Hover Effects**
- **Lore Updates:** Dynamic lore based on context
- **Color Changes:** Subtle color variations
- **Icon Changes:** Context-sensitive icons

### **Loading States**
- **Loading Items:** Use `CLOCK` material with `&e` color
- **Progress Indicators:** Animated progress bars
- **Status Messages:** Clear loading messages

---

## 🏷️ **ICONOGRAPHY**

### **System Icons**
- **Skills:** `DIAMOND_PICKAXE`
- **Collections:** `CHEST`
- **Auction House:** `GOLD_INGOT`
- **Bazaar:** `EMERALD`
- **Bank:** `ENDER_CHEST`
- **Settings:** `COMPARATOR`
- **Profile:** `PLAYER_HEAD`
- **Friends:** `PLAYER_HEAD`
- **Guild:** `BANNER`
- **Dungeons:** `ZOMBIE_HEAD`
- **Slayers:** `SKELETON_SKULL`
- **Fishing:** `FISHING_ROD`
- **Mining:** `DIAMOND_PICKAXE`
- **Farming:** `WHEAT`
- **Foraging:** `OAK_LOG`
- **Combat:** `DIAMOND_SWORD`
- **Enchanting:** `ENCHANTING_TABLE`
- **Alchemy:** `BREWING_STAND`
- **Carpentry:** `CRAFTING_TABLE`
- **Runecrafting:** `END_CRYSTAL`
- **Taming:** `BONE`
- **Social:** `PLAYER_HEAD`
- **Catacombs:** `WITHER_SKELETON_SKULL`

### **Action Icons**
- **Add:** `LIME_DYE`
- **Remove:** `RED_DYE`
- **Edit:** `WRITABLE_BOOK`
- **Delete:** `LAVA_BUCKET`
- **Save:** `EMERALD`
- **Cancel:** `BARRIER`
- **Confirm:** `LIME_STAINED_GLASS_PANE`
- **Back:** `ARROW`
- **Next:** `ARROW`
- **Close:** `BARRIER`
- **Refresh:** `CLOCK`
- **Search:** `COMPASS`
- **Filter:** `HOPPER`
- **Sort:** `ANVIL`
- **Info:** `BOOK`
- **Help:** `WRITTEN_BOOK`
- **Warning:** `YELLOW_DYE`
- **Error:** `RED_DYE`
- **Success:** `LIME_DYE`

---

## 🎨 **ANIMATION GUIDELINES**

### **Transition Effects**
- **Fade In/Out:** Smooth opacity changes
- **Slide:** Smooth position changes
- **Scale:** Smooth size changes
- **Rotation:** Smooth rotation changes

### **Timing**
- **Fast:** 100-200ms for immediate feedback
- **Medium:** 300-500ms for standard transitions
- **Slow:** 600-1000ms for dramatic effects

### **Easing**
- **Ease In:** Slow start, fast finish
- **Ease Out:** Fast start, slow finish
- **Ease In Out:** Slow start and finish, fast middle

---

## 📋 **IMPLEMENTATION CHECKLIST**

### **Before Creating a GUI**
- [ ] Define the purpose and scope
- [ ] Choose appropriate inventory size
- [ ] Plan the layout and navigation
- [ ] Select appropriate colors and icons
- [ ] Define interaction patterns

### **During Implementation**
- [ ] Use consistent color scheme
- [ ] Implement proper borders and fillers
- [ ] Add appropriate lore and descriptions
- [ ] Include navigation buttons
- [ ] Test all interactions

### **After Implementation**
- [ ] Verify visual consistency
- [ ] Test on different screen sizes
- [ ] Validate accessibility
- [ ] Check performance impact
- [ ] Gather user feedback

---

## 🎯 **BEST PRACTICES**

### **Do's**
- ✅ Use consistent color schemes
- ✅ Maintain proper spacing and alignment
- ✅ Provide clear visual hierarchy
- ✅ Include helpful descriptions
- ✅ Test on different screen sizes
- ✅ Use appropriate icons
- ✅ Provide feedback for interactions
- ✅ Keep designs simple and clean

### **Don'ts**
- ❌ Use too many colors
- ❌ Overcrowd the interface
- ❌ Use unclear or confusing icons
- ❌ Forget to include navigation
- ❌ Ignore accessibility
- ❌ Use inconsistent formatting
- ❌ Overcomplicate interactions
- ❌ Forget to test thoroughly

---

## 📚 **EXAMPLES**

### **Main Menu Item**
```java
ItemBuilder.of(Material.DIAMOND_PICKAXE)
    .name("&aSkills")
    .lore(
        "&7Verbessere deine Fähigkeiten",
        "&7und sammle XP in verschiedenen",
        "&7Bereichen wie Bergbau, Kampf, etc.",
        "",
        "&eKlicke zum Öffnen"
    )
    .glow()
    .build()
```

### **Rare Item**
```java
ItemBuilder.of(Material.DIAMOND_SWORD)
    .name("&9Diamond Sword")
    .lore(
        "&7A sharp diamond sword",
        "&7with enhanced durability.",
        "",
        "&9Rare Item",
        "&7Damage: &c+35"
    )
    .enchant(Enchantment.DAMAGE_ALL, 3)
    .build()
```

### **Navigation Button**
```java
ItemBuilder.of(Material.ARROW)
    .name("&eZurück")
    .lore("&7Klicke zum Zurückgehen")
    .build()
```

---

*This design guide ensures consistent, professional, and authentic Hypixel Skyblock-style interfaces throughout the entire project.*
