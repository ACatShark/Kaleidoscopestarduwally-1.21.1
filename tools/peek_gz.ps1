param([string]$Path)
$stream = [IO.File]::OpenRead($Path)
$gz = New-Object IO.Compression.GzipStream($stream, [IO.Compression.CompressionMode]::Decompress)
$reader = New-Object IO.StreamReader($gz)
$lines = New-Object System.Collections.Generic.List[string]
while (-not $reader.EndOfStream) {
    $line = $reader.ReadLine()
    if ($line -match 'unbound value|Reloading ResourceManager|Sound engine started|Backend library|Setting user|Created world|Saving|Stopping|ERROR|FATAL|RegisterEvent|for modid') {
        $lines.Add($line)
    }
    if ($lines.Count -ge 80) { break }
}
$reader.Close(); $gz.Dispose(); $stream.Dispose()
$lines | ForEach-Object { Write-Output $_ }
