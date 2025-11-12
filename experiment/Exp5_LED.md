# Experiment 5 – LED Pattern Control (PIC18F4550)

# Experiment No. 5  
## LED Pattern Control using Switches (PIC18F4550)  
### Name: Sanket Gadekar  
### Roll No.: 304A039  

```c
#include <stdio.h>
#include <stdlib.h>
#include <PIC18F4550.h>

void delay()
{
    unsigned int j;
    for(j=0; j<30000; j++){}
}

void main()
{
    unsigned int key, i;
    key = 0;

    TRISB = 0x00;
    TRISDbits.RD2 = 0;
    TRISAbits.RA4 = 0;
    TRISDbits.RD0 = 1;
    TRISDbits.RD1 = 1;

    while(1)
    {
        if(PORTDbits.RD0 == 0)
            key = 0;

        if(PORTDbits.RD1 == 0)
            key = 1;

        if(key == 0)
        {
            LATDbits.LATD2 = 0;
            LATAbits.LATA4 = 0;
            for(i=0; i<8; i++)
            {
                LATB = 0x01 << i;
                delay();
            }
        }

        if(key == 1)
        {
            LATDbits.LATD2 = 1;
            LATAbits.LATA4 = 1;
            for(i=0; i<8; i++)
            {
                LATB = 0x80 >> i;
                delay();
            }
        }
    }
}
```
