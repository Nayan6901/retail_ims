# Experiment No. 2  
## Waveform Generation using 8051  
### **Name:** Sanket Gadekar  
### **Roll No.:** 304A039  

---

# ✅ **Square Wave Generation using 8051**

```c
// Square Wave Generation using 8051
// Name: Sanket Gadekar 304A039

#include <reg51.h>

// Function Declaration
void delay(void);

void main(void)
{
    while (1)
    {
        P2 = 0x00;   // Output LOW
        delay();

        P2 = 0xFF;   // Output HIGH
        delay();
    }
}

void delay(void)
{
    TMOD = 0x01;    // Timer0, Mode 1 (16-bit)
    TH0  = 0xFC;    // Load timer high byte (for delay)
    TL0  = 0x66;    // Load timer low byte
    TR0  = 1;       // Start Timer0

    while (TF0 == 0);  // Wait for Timer Overflow (TF0=1)

    TR0 = 0;        // Stop Timer
    TF0 = 0;        // Clear overflow flag
}
```

---

# ✅ **Triangular Wave Generation using 8051**

```c
// Triangular Wave Generation using 8051
// Name: Sanket Gadekar 304A039

#include <reg51.h>

// ---------------- Delay Function ----------------
void delay(unsigned int time)
{
    unsigned int x, y;
    for (x = 0; x < time; x++)
        for (y = 0; y < 1200; y++);
}

// ---------------- Main Function ----------------
void main()
{
    P2 = 0x00;   // Initialize Port 2 to 0

    while (1)
    {
        // Rising part of triangular wave
        do
        {
            P2 += 0x05;     // Increase output by step of 5
            delay(50);      // Small delay
        } while (P2 < 0xFF);

        // Falling part of triangular wave
        do
        {
            P2 -= 0x05;     // Decrease output by step of 5
            delay(50);      // Small delay
        } while (P2 > 0x00);
    }
}
```
