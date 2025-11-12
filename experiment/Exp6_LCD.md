# Experiment 6 – LCD Display (PIC18F4550)

# Experiment No. 6  
## LCD Display (PIC18F4550)  
### Name: Sanket Gadekar  
### Roll No.: 304A039  

```c
#include <stdio.h>
#include <stdlib.h>
#include <pic18f4550.h>

#define RS LATCbits.LATC0
#define E  LATCbits.LATC1
#define LCDPORT LATB

void delay()
{
    for(int i=0; i<30000; i++){}
}

void sendcommand(unsigned char command)
{
    LCDPORT = command;
    RS = 0;
    delay(); E = 1;
    delay(); E = 0;
    delay();
}

void sendData(unsigned char data)
{
    LCDPORT = data;
    RS = 1;
    delay(); E = 1;
    delay(); E = 0;
    delay();
}

void main()
{
    TRISB = 0x00;
    TRISCbits.RC0 = 0;
    TRISCbits.RC1 = 0;

    sendcommand(0x38);
    sendcommand(0xC0);
    sendcommand(0x01);
    sendcommand(0x06);
    sendcommand(0x80);

    sendData('V'); sendData('A'); sendData('I'); sendData('S');
    sendData('H'); sendData('N'); sendData('A'); sendData('V'); sendData('I');

    sendcommand(0xC0);
    sendData('3'); sendData('0'); sendData('4');
    sendData('B'); sendData('0'); sendData('3'); sendData('8');
}
```
