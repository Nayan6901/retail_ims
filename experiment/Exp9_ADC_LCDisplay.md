# Experiment 9 – ADC + LCD Display (PIC18F4550)

# Experiment No. 9  
## ADC + LCD Display (PIC18F4550)  
### Name: Sanket Gadekar  
### Roll No.: 304A039  

```c
#include <stdio.h>
#include <p18f4550.h>

#define RS LATCbits.LATC0
#define E  LATCbits.LATC1
#define LCDPORT LATB

void delay()
{
    unsigned int i;
    for(i=0; i<30000; i++){}
}

void sendCommand(unsigned char command)
{
    LCDPORT = command;
    delay();
    RS = 0;
    delay();
    E = 1;
    delay();
    E = 0;
    delay();
}

void sendData(unsigned char data)
{
    LCDPORT = data;
    delay();
    RS = 1;
    delay();
    E = 1;
    delay();
    E = 0;
    delay();
}

void InitLCD(void)
{
    sendCommand(0x38);
    sendCommand(0x01);
    sendCommand(0x0F);
    sendCommand(0x06);
}

void ADCInit()
{
    TRISEbits.RE2 = 1;
    ADCON0 = 0b00011101;
    ADCON1 = 0b00000111;
    ADCON2 = 0b10101110;
}

unsigned short Read_ADC()
{
    GODONE = 1;
    while(GODONE == 1);
    return ADRES;
}

void DisplayResult(unsigned short ADCVal)
{
    unsigned char i, text[16];
    unsigned short tempv;
    tempv = ADCVal;

    ADCVal = (5500/1024) * tempv;
    sprintf(text, "%04dmv", ADCVal);

    sendCommand(0x80);
    for(i=0; i<6; i++)
        sendData(text[i]);

    sendCommand(0xC0);
    for(i=0; i<10; i++)
    {
        if(tempv & 0x200)
            sendData('1');
        else
            sendData('0');

        tempv <<= 1;
    }
}

void main()
{
    unsigned short Ch_result;

    TRISB = 0x00;
    TRISCbits.RC0 = 0;
    TRISCbits.RC1 = 0;

    ADCInit();
    InitLCD();

    while(1)
    {
        Ch_result = Read_ADC();
        DisplayResult(Ch_result);
        delay();
        delay();
    }
}
```
