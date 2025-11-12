# Experiment No. 3  
## Stepper Motor – Clockwise & Anticlockwise Rotation  
### **Name:** Sanket Gadekar  
### **Roll No.:** 304A039  

---

## ✅ **Anticlockwise Rotation of Stepper Motor (8051)**

```c
// Sanket Gadekar 304A039
// ANTICLOCKWISE ROTATION OF STEPPER MOTOR

#include <reg51.h>

// ---------------- Delay Function ----------------
void delay(void)
{
    unsigned int i;
    for (i = 0; i < 50000; i++);   // Software delay loop
}

// ---------------- Main Function ----------------
void main(void)
{
    while (1)
    {
        P0 = 0x08;   // Step 1: Energize coil D
        delay();

        P0 = 0x04;   // Step 2: Energize coil C
        delay();

        P0 = 0x02;   // Step 3: Energize coil B
        delay();

        P0 = 0x01;   // Step 4: Energize coil A
        delay();
    }
}
```

---

## ✅ **Clockwise Rotation of Stepper Motor (8051)**

```c
// Sanket Gadekar 304A039
// CLOCKWISE ROTATION OF STEPPER MOTOR

#include <reg51.h>

void delay();

void main() {
    while(1) {
        P0 = 0x01;   // Step 1: Coil A
        delay();
        P0 = 0x02;   // Step 2: Coil B
        delay();
        P0 = 0x04;   // Step 3: Coil C
        delay();
        P0 = 0x08;   // Step 4: Coil D
        delay();
    }
}

void delay() {
    unsigned int i;
    for(i = 0; i < 500; i++);  // Software delay
}
```
