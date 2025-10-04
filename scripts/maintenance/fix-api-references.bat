@echo off
echo Fixing Bukkit API references...

REM Fix PotionEffectType references
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'PotionEffectType\.INCREASE_DAMAGE', 'PotionEffectType.STRENGTH' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'PotionEffectType\.DAMAGE_RESISTANCE', 'PotionEffectType.RESISTANCE' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'PotionEffectType\.SLOW', 'PotionEffectType.SLOWNESS' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'PotionEffectType\.JUMP', 'PotionEffectType.JUMP_BOOST' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'PotionEffectType\.FAST_DIGGING', 'PotionEffectType.HASTE' | Set-Content -Path 'temp_fix.txt'"

REM Fix Particle references
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Particle\.VILLAGER_HAPPY', 'Particle.HAPPY_VILLAGER' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Particle\.ENCHANTMENT_TABLE', 'Particle.ENCHANT' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Particle\.WATER_SPLASH', 'Particle.SPLASH' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Particle\.FIREWORKS_SPARK', 'Particle.FIREWORK' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Particle\.SMOKE_LARGE', 'Particle.LARGE_SMOKE' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Particle\.SMOKE_NORMAL', 'Particle.SMOKE' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Particle\.EXPLOSION_LARGE', 'Particle.EXPLOSION_EMITTER' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Particle\.REDSTONE', 'Particle.REDSTONE' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Particle\.TOTEM', 'Particle.TOTEM' | Set-Content -Path 'temp_fix.txt'"

REM Fix Enchantment references
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Enchantment\.DAMAGE_ALL', 'Enchantment.SHARPNESS' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Enchantment\.DAMAGE_UNDEAD', 'Enchantment.SMITE' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Enchantment\.DAMAGE_ARTHROPODS', 'Enchantment.BANE_OF_ARTHROPODS' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Enchantment\.LUCK', 'Enchantment.LUCK_OF_THE_SEA' | Set-Content -Path 'temp_fix.txt'"

REM Fix EntityType references
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'EntityType\.SNOWMAN', 'EntityType.SNOW_GOLEM' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'EntityType\.MUSHROOM_COW', 'EntityType.MOOSHROOM' | Set-Content -Path 'temp_fix.txt'"

REM Fix Attribute references
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'Attribute\.GENERIC_MAX_HEALTH', 'Attribute.GENERIC_MAX_HEALTH' | Set-Content -Path 'temp_fix.txt'"

REM Fix PotionType references
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'PotionType\.INSTANT_HEAL', 'PotionType.HEALING' | Set-Content -Path 'temp_fix.txt'"
powershell -Command "(Get-Content -Path 'src\main\java\de\noctivag\plugin\**\*.java' -Recurse) -replace 'PotionType\.SPEED', 'PotionType.SWIFTNESS' | Set-Content -Path 'temp_fix.txt'"

echo API references fixed!
del temp_fix.txt
