# Generates 16x16 placeholder PNG textures for the livestock/economy items & block.
# Run from Windows PowerShell:  powershell.exe -NoProfile -ExecutionPolicy Bypass -File tools/generate_placeholders.ps1
Add-Type -AssemblyName System.Drawing

$root = 'c:/Users/17046/Downloads/kaleidoscopestarduwally-1.21.1'
$assets = Join-Path $root 'src/main/resources/assets/kaleidoscope_starduwally/textures'

function New-Bitmap([string]$rel) {
    $full = Join-Path $assets ($rel + '.png')
    $dir = Split-Path $full -Parent
    if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
    $bmp = New-Object System.Drawing.Bitmap 16, 16
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.Clear([System.Drawing.Color]::Transparent)
    return @($bmp, $g, $full)
}

function Close-Bmp($ctx) {
    $ctx[1].Dispose()
    $ctx[0].Save($ctx[2], [System.Drawing.Imaging.ImageFormat]::Png)
    $ctx[0].Dispose()
    Write-Output ('wrote ' + $ctx[2])
}

function Write-Generic([string]$rel, [int[]]$rgb, [string]$shape) {
    $ctx = New-Bitmap $rel
    $bmp = $ctx[0]; $g = $ctx[1]
    $brush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255, $rgb[0], $rgb[1], $rgb[2]))
    switch ($shape) {
        'seed'   { $g.FillEllipse($brush, 5, 4, 6, 8) }              # vertical seed
        'feed'   { $g.FillEllipse($brush, 4, 5, 8, 6) }              # horizontal kernel
        'paper'  { $g.FillRectangle($brush, 2, 3, 12, 10) }          # paper/contract
        'block'  {
            $g.FillRectangle($brush, 0, 0, 16, 16)
            $dark = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255, [Math]::Max(0, $rgb[0] - 50), [Math]::Max(0, $rgb[1] - 50), [Math]::Max(0, $rgb[2] - 50)))
            $g.FillRectangle($dark, 0, 10, 16, 6)
            $dark.Dispose()
        }
        'coin'   {
            # ring with darker center
            $g.FillEllipse($brush, 3, 3, 10, 10)
            $inner = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255, [Math]::Max(0, $rgb[0] - 70), [Math]::Max(0, $rgb[1] - 70), [Math]::Max(0, $rgb[2] - 70)))
            $g.CompositingMode = [System.Drawing.Drawing2D.CompositingMode]::SourceCopy
            $g.FillEllipse($inner, 5, 5, 6, 6)
            $inner.Dispose()
        }
        default  { $g.FillEllipse($brush, 3, 3, 10, 10) }             # generic pill
    }
    $hl = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(110, 255, 255, 255))
    if ($shape -eq 'paper') {
        $g.FillRectangle($hl, 4, 5, 8, 1)
        $g.FillRectangle($hl, 4, 8, 8, 1)
    } elseif ($shape -eq 'block') {
        $g.FillRectangle($hl, 0, 0, 16, 1)
    } else {
        $g.FillEllipse($hl, 5, 4, 3, 2)
    }
    $hl.Dispose(); $brush.Dispose()
    Close-Bmp $ctx
}

# Items
Write-Generic 'item/livestock_feed'  @(150,100, 60) 'feed'
Write-Generic 'item/poultry_feed'    @( 96,134, 70) 'feed'
Write-Generic 'item/premium_feed'    @(232,172, 46) 'feed'
Write-Generic 'item/sale_contract'   @(246,246,250) 'paper'
Write-Generic 'item/release_contract'@(198,210,224) 'paper'

# Block texture (full square)
Write-Generic 'block/shipping_bin'   @(150,105, 65) 'block'

# ---- 作物生长阶段贴图（随 age 长高，成熟阶段带果实/花穗） ----
function Write-CropStage([string]$crop, [int]$stage, [int[]]$leaf, [int[]]$fruit) {
    $ctx = New-Bitmap "block/crop/$crop/stage$stage"
    $bmp = $ctx[0]; $g = $ctx[1]
    $green = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255, $leaf[0], $leaf[1], $leaf[2]))
    # 茎：随阶段长高（底部对齐 16px 贴图）
    $h = 4 + $stage * 2          # 4..14
    $top = 16 - $h
    $g.FillRectangle($green, 7, $top, 2, $h)
    # 叶片
    if ($stage -ge 1) { $g.FillRectangle($green, 4, $top + 2, 3, 2) }
    if ($stage -ge 3) { $g.FillRectangle($green, 9, $top + 4, 3, 2) }
    # 成熟阶段（4/5）结出果实/花穗
    if ($stage -ge 4) {
        $fb = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255, $fruit[0], $fruit[1], $fruit[2]))
        $g.FillEllipse($fb, 5, $top + 4, 4, 4)
        if ($stage -eq 5) { $g.FillEllipse($fb, 8, $top + 1, 4, 4) }
        $fb.Dispose()
    }
    $green.Dispose()
    Close-Bmp $ctx
}

Write-CropStage 'ancient_fruit' 0 @( 96,150, 78) @(150, 70,170)
Write-CropStage 'ancient_fruit' 1 @( 96,150, 78) @(150, 70,170)
Write-CropStage 'ancient_fruit' 2 @( 96,150, 78) @(150, 70,170)
Write-CropStage 'ancient_fruit' 3 @( 96,150, 78) @(150, 70,170)
Write-CropStage 'ancient_fruit' 4 @( 96,150, 78) @(150, 70,170)
Write-CropStage 'ancient_fruit' 5 @( 96,150, 78) @(150, 70,170)

Write-CropStage 'hops' 0 @(120,168, 84) @(214,206, 92)
Write-CropStage 'hops' 1 @(120,168, 84) @(214,206, 92)
Write-CropStage 'hops' 2 @(120,168, 84) @(214,206, 92)
Write-CropStage 'hops' 3 @(120,168, 84) @(214,206, 92)
Write-CropStage 'hops' 4 @(120,168, 84) @(214,206, 92)
Write-CropStage 'hops' 5 @(120,168, 84) @(214,206, 92)

# 韭葱（蔬菜）
Write-Generic 'item/leek'                @(150,200,120) 'feed'

# 作物产物 / 种子贴图
Write-Generic 'item/ancient_fruit'       @(150, 70,170) 'feed'
Write-Generic 'item/ancient_fruit_seeds' @(186,140,196) 'seed'
Write-Generic 'item/hops'                @(214,206, 92) 'feed'
Write-Generic 'item/hops_seeds'          @(176,180,110) 'seed'
