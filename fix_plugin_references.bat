@echo off
echo Fixing Plugin class references to SkyblockPlugin...

REM Update Plugin class references to SkyblockPlugin
for /r "src\main\java\de\noctivag\skyblock" %%f in (*.java) do (
    powershell -Command "(Get-Content '%%f') -replace 'Plugin\.class', 'SkyblockPlugin.class' | Set-Content '%%f'"
    powershell -Command "(Get-Content '%%f') -replace 'Plugin plugin', 'SkyblockPlugin plugin' | Set-Content '%%f'"
    powershell -Command "(Get-Content '%%f') -replace 'Plugin this\.plugin', 'SkyblockPlugin this.plugin' | Set-Content '%%f'"
    powershell -Command "(Get-Content '%%f') -replace 'Plugin\s+\w+\s*=', 'SkyblockPlugin ' | Set-Content '%%f'"
    powershell -Command "(Get-Content '%%f') -replace 'new Plugin\(\)', 'new SkyblockPlugin()' | Set-Content '%%f'"
    powershell -Command "(Get-Content '%%f') -replace 'Plugin\.getInstance\(\)', 'SkyblockPlugin.getInstance()' | Set-Content '%%f'"
)

echo Plugin references updated!
