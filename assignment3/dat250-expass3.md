# Technical problems
When checking the SHA-256 checksum of my installation package, i first got a "False" comparison between my MongoDB installer hash and the signature file.
This was because i had the SHA256 signature of the 4.4.0 version that the tutorial provided, while having the 4.4.1 version of the installer.
After getting the signature for the right version, i got a "True" comparison, and the package was validated. 
