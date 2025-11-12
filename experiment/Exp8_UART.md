# Experiment 8 – UART Echo (PIC18F4550)

# Experiment No. 8  
## UART Echo Program (PIC18F4550)  
### Name: Sanket Gadekar  
### Roll No.: 304A039  

```c
#include <p18F4550.h>
#include <stdio.h>

void InitUART()
{
    TRISCbits.RC6 = 0;
    TRISCbits.RC7 = 1;
    SPBRG = 77;

    TXSTA = 0b00100000;
    RCSTA = 0b10010000;
}

void SendChar(unsigned char data)
{
    while(TXIF == 0);
    TXREG = data;
}

unsigned char GetChar(void)
{
    while(!RCIF);
    return RCREG;
}

void main(void)
{
    unsigned char key;
    InitUART();

    while(1)
    {
        key = GetChar();
        SendChar(key);
    }
}
```
