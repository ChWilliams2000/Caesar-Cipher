# Caesar-Cipher
Personal project for encrypting, decrypting, and breaking Caesar shifts.

Encryption and decryption work by shifting the letters by the given value.

Breaking works by generating all 26 possible solutions and calculating their likelihood based on letter frequency.
Stores letter frequency values in a HashMap for more efficient runtime when calculating overall "score" of a possible solution.
The most likely solution is the one with the highest "score" (total of all letter frequencies of that variation).
