# Fix BOM encoding issues in Java files
Write-Host "Fixing BOM encoding issues in Java files..."

# Get all Java files
$javaFiles = Get-ChildItem -Path "src" -Filter "*.java" -Recurse

foreach ($file in $javaFiles) {
    try {
        Write-Host "Processing: $($file.FullName)"
        
        # Read content as UTF8
        $content = Get-Content -Path $file.FullName -Encoding UTF8 -Raw
        
        # Remove BOM if present
        if ($content.StartsWith([char]0xFEFF)) {
            $content = $content.Substring(1)
        }
        
        # Write back without BOM
        [System.IO.File]::WriteAllText($file.FullName, $content, [System.Text.UTF8Encoding]::new($false))
        
        Write-Host "Fixed: $($file.Name)"
    }
    catch {
        Write-Host "Error processing $($file.Name): $($_.Exception.Message)"
    }
}

Write-Host "Encoding fix completed!"
