# All Codes Combined (Markdown)

## Stepper Motor Anticlockwise (8051)
```c
// Sanket Gadekar 304A039
// ANTICLOCKWISE ROTATION OF STEPPER MOTOR

#include <reg51.h>

void delay(void)
{
    unsigned int i;
    for (i = 0; i < 50000; i++);
}

void main(void)
{
    while (1)
    {
        P0 = 0x08;
        delay();
        P0 = 0x04;
        delay();
        P0 = 0x02;
        delay();
        P0 = 0x01;
        delay();
    }
}
```

## Alternate Flashing Program
```c
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
        P1 = 0xAA;
        delay(Del);
        P1 = 0x55;
        delay(Del);
    }
}
```

## BCD Counter Program
```c
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
    unsigned char count[10]={0,1,2,3,4,5,6,7,8,9};
    unsigned int x;

    P1 = 0x00;
    while(1)
    {
        for(x=0;x<10;x++)
        {
            P1 = count[x];
            delay(1000);
        }
    }
}
```

## Experiment 9 – ADC + LCD (PIC18F4550)
```c
#include<stdio.h>
#include <p18f4550.h>
#define RS LATCbits.LATC0
#define E LATCbits.LATC1
#define LCDPORT LATB

void delay()
{
    unsigned int i;
    for(i=0;i<30000;i++){}
}

void sendCommand(unsigned char command)
{
    LCDPORT=command;
    delay();
    RS=0;
    delay();
    E=1;
    delay();
    E=0;
    delay();
}

void sendData(unsigned char data)
{
    LCDPORT=data;
    delay();
    RS=1;
    delay();
    E=1;
    delay();
    E=0;
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
    while(GODONE==1);
    return ADRES;
}

void DisplayResult(unsigned short ADCVal)
{
    unsigned char i,text[16];
    unsigned short tempv;
    tempv = ADCVal;

    ADCVal = (5500/1024)*tempv;
    sprintf(text,"%04dmv",ADCVal);

    sendCommand(0x80);
    for(i=0;i<6;i++)
        sendData(text[i]);

    sendCommand(0xC0);
    for(i=0;i<10;i++)
    {
        if(tempv & 0x200)
            sendData('1');
        else
            sendData('0');
        tempv <<=1;
    }
}

void main()
{
    unsigned short Ch_result;

    TRISB=0x00;
    TRISCbits.RC0=0;
    TRISCbits.RC1=0;
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

## Experiment 6 – LCD Display (PIC18F4550)
```c
#include <stdio.h>
#include<stdlib.h>
#include<pic18f4550.h>
#define RS LATCbits.LATC0
#define E LATCbits.LATC1
#define LCDPORT LATB

void delay()
{
    for(int i=0;i<30000;i++){}
}

void sendcommand(unsigned char command)
{
    LCDPORT = command;
    RS=0;
    delay(); E=1;
    delay(); E=0;
    delay();
}

void sendData(unsigned char data)
{
    LCDPORT=data;
    RS=1;
    delay(); E=1;
    delay(); E=0;
    delay();
}

void main()
{
    TRISB=0x00;
    TRISCbits.RC0=0;
    TRISCbits.RC1=0;

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

## Experiment 5 – LED Pattern + Switch (PIC18F4550)
```c
#include <stdio.h>
#include <stdlib.h>
#include<PIC18F4550.h>

void delay()
{
    unsigned int j;
    for(j=0;j<30000;j++){}
}

void main()
{
    unsigned int key,i;
    key=0;

    TRISB=0x00;
    TRISDbits.RD2=0;
    TRISAbits.RA4=0;
    TRISDbits.RD0=1;
    TRISDbits.RD1=1;

    while(1)
    {
        if(PORTDbits.RD0==0) key=0;
        if(PORTDbits.RD1==0) key=1;

        if(key==0)
        {
            LATDbits.LATD2=0;
            LATAbits.LATA4=0;
            for(i=0;i<8;i++)
            {
                LATB=0x01<<i;
                delay();
            }
        }
        if(key==1)
        {
            LATDbits.LATD2=1;
            LATAbits.LATA4=1;
            for(i=0;i<8;i++)
            {
                LATB=0x80>>i;
                delay();
            }
        }
    }
}
```

## Experiment 7 – Timer Interrupt (PIC18F4550)
```c
#include<p18f4550.h>

void timerInit(void)
{
    T0CON = 0b00000111;
    TMR0H = 0xED;
    TMR0L = 0xB0;
}

void Interrupt_Init(void)
{
    IPEN = 1;
    INTCON = 0b11100000;
    TMR0IP = 0;
}

void interrupt low_priority timerinterrupt(void)
{
    if(TMR0IF==1)
    {
        TMR0ON = 0;
        TMR0IF = 0;
        LATB =~ LATB;
        TMR0H = 0xED;
        TMR0L = 0xB0;
        TMR0ON = 1;
    }
}

void main(void)
{
    TRISB=0x00;
    LATB = 0xFF;

    Interrupt_Init();
    timerInit();
    TMR0ON=1;

    while(1);
}
```

## Experiment 8 – UART Echo (PIC18F4550)
```c
#include<p18F4550.h>
#include<stdio.h>

void InitUART()
{
    TRISCbits.RC6=0;
    TRISCbits.RC7=1;
    SPBRG=77;
    TXSTA=0b00100000;
    RCSTA=0b10010000;
}

void SendChar(unsigned char data)
{
    while(TXIF==0);
    TXREG=data;
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
        key=GetChar();
        SendChar(key);
    }
}
```

## Stepper Motor Clockwise (8051)
```c
// Sanket Gadekar 304A039
// CLOCKWISE ROTATION OF STEPPER MOTOR

#include <reg51.h>
void delay();

void main() {
    while(1) {
        P0 = 0x01;
        delay();
        P0 = 0x02;
        delay();
        P0 = 0x04;
        delay();
        P0 = 0x08;
        delay();
    }
}

void delay() {
    unsigned int i;
    for(i = 0; i < 500; i++);
}
```

## HEX Counter Program (8051)
```c
#include <reg51.h>
#define Del 2000

void delay(unsigned int time)
{
    unsigned int i, j;
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
        for(x=0;x<16;x++)
        {
            P1 = count[x];
            delay(1000);
        }
    }
}
```

## Flash All LEDs (8051)
```c
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
        P1 = 0xFF;
        delay(300);
        P1 = 0x00;
        delay(300);
    }
}
```
