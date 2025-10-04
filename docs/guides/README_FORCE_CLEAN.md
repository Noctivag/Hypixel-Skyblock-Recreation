Force-clean scripts for Windows

Purpose
- Help remove the `target` directory when `mvn clean` fails with "Failed to delete ..." due to locked files on Windows.
- Provide a PowerShell script (`force-clean.ps1`) with retries, attribute fixes, and a hint to check for file locks.
- Provide a CMD wrapper (`force-clean.cmd`) so you can run the script from a normal Windows command prompt.

Files added
- `scripts\force-clean.ps1` - PowerShell script that attempts to clear read-only attributes, retries removal, and optionally uses Sysinternals `handle.exe` to list locking processes.
- `scripts\force-clean.cmd` - Simple batch wrapper that runs the PowerShell script from `cmd.exe`.

Usage examples (from a Windows cmd.exe)
- Delete the default `target` folder in the repository root:
  force-clean.cmd

- Delete a specific folder (e.g., the classes folder):
  force-clean.cmd "E:\\Basics Plugin\\target\\classes"

- Increase retries and delay between attempts:
  force-clean.cmd "E:\\Basics Plugin\\target" 20 2

What the script does
1. Resolves the target path (default `.\target`). If it doesn't exist it exits successfully.
2. Recursively clears file attributes (so read-only files won't block deletion).
3. Tries up to `Retries` times to `Remove-Item -Recurse -Force`. Between attempts it sleeps and clears attributes again.
4. If deletion still fails, it looks for `handle.exe` (Sysinternals) in common locations and runs it to list processes with open handles to the path. If `handle.exe` isn't found it prints guidance to use `resmon` or Process Explorer.

Common causes of the Maven "Failed to delete" error
- IntelliJ or another IDE has compiled classes or a debugger attached.
- A running Minecraft server or plugin tester keeps classes loaded.
- An external process (indexer, antivirus) briefly locks files.

Next steps to resolve your Maven failure
1. Close IntelliJ or disable its auto-build temporarily.
2. Stop any running server or plugin runner that may be using the plugin classes.
3. Run from cmd.exe in the repo root:
   force-clean.cmd
4. Re-run `mvn clean` (or your regular build):
   mvn clean package

If deletion still fails
- Use Process Explorer or Resource Monitor to find the process locking files and stop it.
- Install Sysinternals and re-run `force-clean.cmd` so the script can call `handle.exe` and display locking processes.
- If you can't stop a process, a reboot will clear locks.

Safety notes
- The script forcibly deletes the target folder. Only point it at paths you intend to remove.
- If you need to preserve build artifacts, copy them elsewhere before running.

If you'd like, I can also:
- Add a Gradle/Maven wrapper target or a small PowerShell function to stop common IDE processes before cleaning.
- Try to detect running JVMs with open handles programmatically and prompt the user.

