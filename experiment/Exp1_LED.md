# Experiment No. 1  
## LED Interfacing using 8051  
### **Name:** Sanket Gadekar  
### **Roll No.:** 304A039  

---

# ✅ **1. Flash All LEDs Program**

```c
// FLASH ALL LEDS PROGRAM
#include <reg51.h>
#define Del 300

void delay(unsigned int time)
{
    unsigned int i, j;
    for(i=0;i<time;i++)
        for(j=0;j<100;j++);
}

void main(void)
{
    while(1)
    {
        P1 = 0xFF;   // All LEDs ON
        delay(300);

        P1 = 0x00;   // All LEDs OFF
        delay(300);
    }
}
```

---

# ✅ **2. Alternate LED Flashing Program**

```c
// ALTERNATE FLASHING PROGRAM
#include <reg51.h>
#define Del 300

void delay(unsigned int time)
{
    unsigned int i, j;
    for(i=0;i<time;i++)
        for(j=0;j<100;j++);
}

void main(void)
{
    while(1)
    {
        P1 = 0xAA;   // 10101010
        delay(Del);

        P1 = 0x55;   // 01010101
        delay(Del);
    }
}
```

---

# ✅ **3. BCD Counter (0–9) on LEDs**

```c
// BCD COUNTER PROGRAM
#include <reg51.h>
#define Del 2000

void delay(unsigned int time)
{
    unsigned int i,j;
    for(i=0;i<time;i++)
        for(j=0;j<5000;j++);
}

void main(void)
{
    unsigned char count[10] = {0,1,2,3,4,5,6,7,8,9};
    unsigned int x;

    P1 = 0x00;

    while(1)
    {
        for(x=0; x<10; x++)
        {
            P1 = count[x];
            delay(1000);
        }
    }
}
```

---

# ✅ **4. HEX Counter (0–F) on LEDs**

```c
// HEX COUNTER PROGRAM
#include <reg51.h>
#define Del 2000

void delay(unsigned int time)
{
    unsigned int i,j;
    for(i=0;i<time;i++)
        for(j=0;j<5000;j++);
}

void main(void)
{
    unsigned char count[16] =
       {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,
        0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F};
    unsigned int x;

    P1 = 0x00;

    while(1)
    {
        for(x=0; x<16; x++)
        {
            P1 = count[x];
            delay(1000);
        }
    }
}
```
