# Generates 16x16 placeholder PNG textures for all crops/items of this mod.
# Replace these PNGs later with real art. Run from PowerShell (Windows).
Add-Type -AssemblyName System.Drawing

$assets = 'c:/Users/17046/Downloads/kaleidoscopestarzhuvally-template-1.21.1/src/main/resources/assets/kaleidoscopestarzhuvally/textures'

function Write-Png([string]$rel, [int[]]$rgb, [switch]$item, [int]$stage) {
    $full = Join-Path $assets ($rel + '.png')
    $dir = Split-Path $full -Parent
    if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
    $bmp = New-Object System.Drawing.Bitmap 16, 16
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.Clear([System.Drawing.Color]::Transparent)
    $brush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255, $rgb[0], $rgb[1], $rgb[2]))
    if ($item) {
        $g.FillEllipse($brush, 3, 3, 10, 10)
        $hl = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(130, 255, 255, 255))
        $g.FillEllipse($hl, 5, 4, 3, 2)
        $hl.Dispose()
    } else {
        $h = 6 + $stage * 3
        if ($h -gt 16) { $h = 16 }
        $g.FillEllipse($brush, 3, 16 - $h, 10, $h)
    }
    $brush.Dispose()
    $g.Dispose()
    $bmp.Save($full, [System.Drawing.Imaging.ImageFormat]::Png)
    $bmp.Dispose()
    Write-Output ('wrote {0}' -f $rel)
}

# Garlic crop stages (light green -> pale bulb)
Write-Png 'block/crop/garlic/stage0' @(162,201,150) -stage 0
Write-Png 'block/crop/garlic/stage1' @(182,212,156) -stage 1
Write-Png 'block/crop/garlic/stage2' @(200,218,166) -stage 2
Write-Png 'block/crop/garlic/stage3' @(214,220,180) -stage 3

# Cauliflower crop stages (green -> cream head)
Write-Png 'block/crop/cauliflower/stage0' @(146,186,140) -stage 0
Write-Png 'block/crop/cauliflower/stage1' @(170,206,152) -stage 1
Write-Png 'block/crop/cauliflower/stage2' @(190,214,162) -stage 2
Write-Png 'block/crop/cauliflower/stage3' @(212,218,176) -stage 3
Write-Png 'block/crop/cauliflower/stage4' @(240,238,228) -stage 4

# Kale crop stages (deep green)
Write-Png 'block/crop/kale/stage0' @(64,118,70) -stage 0
Write-Png 'block/crop/kale/stage1' @(60,132,76) -stage 1
Write-Png 'block/crop/kale/stage2' @(56,146,84) -stage 2
Write-Png 'block/crop/kale/stage3' @(54,158,92) -stage 3

# Items
Write-Png 'item/garlic' @(242,236,218) -item
Write-Png 'item/garlic_clove' @(224,200,158) -item
Write-Png 'item/cauliflower' @(248,246,240) -item
Write-Png 'item/cauliflower_seeds' @(196,176,116) -item
Write-Png 'item/kale' @(92,146,88) -item
Write-Png 'item/kale_seeds' @(100,80,60) -item
