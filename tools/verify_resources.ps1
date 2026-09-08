# Quick sanity check: parse all our JSON (UTF-8) and verify model/texture cross-references exist.
$root = 'c:/Users/17046/Downloads/kaleidoscopestarzhuvally-template-1.21.1/src/main/resources'
$ns = 'kaleidoscopestarzhuvally'
$errors = @()

function Read-JsonFile([string]$path) {
    $text = [System.IO.File]::ReadAllText($path)   # decodes as UTF-8
    return ($text | ConvertFrom-Json)
}

function Test-Json([string]$path) {
    try { Read-JsonFile $path | Out-Null } catch { $script:errors += "BAD JSON: $path :: $($_.Exception.Message)" }
}

# 1) parse every json under assets & data
Get-ChildItem -Path "$root/assets/$ns", "$root/data/$ns" -Recurse -Filter *.json | ForEach-Object { Test-Json $_.FullName }

$assetsRoot = "$root/assets/$ns"

# 2) every blockstate model reference must exist
Get-ChildItem "$assetsRoot/blockstates" -Filter *.json | ForEach-Object {
    $j = Read-JsonFile $_.FullName
    $j.variants.PSObject.Properties.Value | ForEach-Object {
        $m = [string]$_.model
        if ($m -like 'minecraft:*') { return }
        if ($m -like "$ns`:*") { $m = $m.Substring($ns.Length + 1) }
        $p = "$assetsRoot/models/$m.json"
        if (-not (Test-Path $p)) { $script:errors += "MISSING model: $($_.model) (referenced by $($_.FullName))" }
    }
}

# 3) every model texture reference must exist
Get-ChildItem "$assetsRoot/models" -Recurse -Filter *.json | ForEach-Object {
    $j = Read-JsonFile $_.FullName
    foreach ($t in @($j.textures.PSObject.Properties.Value)) {
        if ([string]$t -like 'minecraft:*') { continue }
        if ([string]$t -like '#*') { continue }
        $rel = (($t -split ':', 2)[1])
        $p = "$assetsRoot/textures/$rel.png"
        if (-not (Test-Path $p)) { $script:errors += "MISSING texture: $t (referenced by $($_.FullName))" }
    }
}

if ($errors.Count -eq 0) { Write-Output 'OK: all JSON valid, all model/texture references resolve.' }
else { $errors | ForEach-Object { Write-Output $_ }; exit 1 }
