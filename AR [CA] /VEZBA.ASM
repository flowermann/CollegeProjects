! inicijalizacija simulacije
dc x.1, x.9996
dc x.0, x.9998
onkp false, x.1, x.1
onkp false, x.1, x.1
onkp true, x.1, x.1
ondma x.1, x.1
ondma x.1, x.1
ondma x.1, x.1
ondma x.1, x.1
kpreg 1.1, r0, x.1
kpreg 1.1, r1, x.2
kpreg 1.1, r2, x.3
kpreg 1.1, r3, x.4
kpreg 1.1, r4, x.5
kpreg 1.1, r5, x.6
kpreg 1.1, r6, x.7
kpreg 1.1, r7, x.8
kpreg 2.1, r0, x.9
kpreg 2.1, r1, x.a
kpreg 2.1, r2, x.b
kpreg 2.1, r3, x.c
kpreg 2.1, r4, x.d
kpreg 2.1, r5, x.e
kpreg 2.1, r6, x.f
kpreg 2.1, r7, x.10
reg pc, x.4000
reg ivtp, x.4000
reg sp, x.9000
! kraj inicijalizacije

!resenje
org x.4000
ldimm x.100, r0
mvrir r0,ivtp !ivtp = 100h

!inicijalizacija iv tabele
ldimm x.3000, r0
stmem x.100, r0 !broj ulaza 0, DMA 1.4
ldimm x.2500, r0
stmem x.101, r0 !broj ulaza 1, DMA 1.2
ldimm x.2000, r0 
stmem x.103 , r0 !broj ulaza 3, DMA 1.1
ldimm x.1500, r0 
stmem x.102, r0 !broj ulaza 2, KP 2.1
ldimm x.1000, r0
stmem x.104, r0 !broj ulaza 4, KP 1.2
ldimm x.500, r0
stmem x.105, r0 !broj ulaza 5, KP 1.1

ldimm x.5000, r1 !ptrA
ldimm x.6000, r2 !ptrB
ldimm x.8, r3 !cntA
ldimm x.8, r4 !cntB
clr r5 !semafor za prekidnu rutinu KP 1.1, niz A

!paljenje periferije KP 1.1 (1111)
ldimm x.5, r6
stmem x.f102, r6 !broj ulaza 5 u ie registar
ldimm x.f ,r6
stmem x.f100, r6 !menjanje control registra

!paljenje periferije KP 2.1
ldimm x.5 ,r6
stmem x.f200, r6

inte !kako bi obezbedili da KP 1.1 moze da salje prekide
!ispitivanje ready bita KP 2.1
ldimm x.1,r6
wait21:
ldmem x.F201,r7         ! proveravamo statusni registar KP 2.1
and r7,r7,r6           
beql wait21             ! ako je 0, vrti se i cekaj 

! Ready bit je 1, hvatamo podatak sa KP 2.1
ldmem x.F203,r0
stri [r2],r0
inc r2
dec r4
bneq wait21             ! vrti dok ne upišem svih 8 elemenata niza B

! kp 2.1 gotov, iskljucimo ga
ldimm x.0,r0
stmem x.F200,r0

! cekamo da se KP 1.1 sam ugasi ( u prekidnoj rutini)
ldimm x.0,r9
loop11: 
ldmem x.F100,r8
cmp r8,r9
bneq loop11


end:
!priprema za poziv potprograma (stavljamo argumente funkcije na stek u obrnutom redosledu)
ldimm x.8, r0
push r0 !argument N
ldimm x.6000, r0
push r0 !argument ptrB
ldimm x.5000, r0
push r0 !argument ptrA


jsr sumAll
pop ra !popujem u rA kako ne bi izgubio moj rezultat koji je sacuvan u r0
pop ra
pop ra

stmem x.9999, r0 !upisujemo rezultat sabiranja nizova u memoriju

!aktiviranje DMA 1.4 
ldimm x.8, ra 
stmem x.f0c4, ra !count
ldimm x.6000, ra
stmem x.f0c5, ra !izvorisna adresa A1
ldimm x.6100, ra
stmem x.f0c6, ra !destinaciona adresa A2
ldimm x.0, ra
stmem x.f0c2, ra !entry registar
ldimm x.0, rb !semafor
ldimm x.be , ra
stmem x.f0c0, ra !palim dma 1.4 (1011 1110)
!provera da li je dma1.4 zavrsio posao (provera semafora)
ldimm x.1, ra
loopdma14:
cmp rb, ra       
bneq loopdma14


!aktiviranje KP 1.2
ldimm x.4, ra !podesavanje entry registra (broj ulaza 4)
stmem x.f142, ra
ldimm x.5000, r1 !ptrA
ldimm x.8, rf !cntA
clr r5 !semafor 
ldimm x.e, ra !podesavanje control registra (1110)
stmem x.f140, ra

!incijalizacija dma 1.2
ldimm x.1, ra
stmem x.f044, ra !count dma 1.2
ldimm x.9999, ra
stmem x.f045, ra !source za dma1.2
ldimm x.1, ra
stmem x.f042, ra !entry registar 
ldimm x.0, r9 !semafor za dma1.2
ldimm x.8e, ra
stmem x.f040, ra !paljenje kontrolere dma1.2

! provera da li je dma1.2 zavrsio posao
ldimm x.1,ra
loopdma12:
cmp r9, ra !provera da li je semafor dma 1.2 = 1
bneq loopdma12

! provera da li je kp1.2 zavrsio posao
ldimm x.0,r9
wait12: ldmem x.F140,r8
cmp r8,r9 !provera da li je periferija ugasena (u prekidnoj rutini)
bneq wait12


! kraj programa
intd
halt


!potprogram sumAll -----------------------------------------------------
sumAll: 
push r7 !old base pointer
mvrpl r7 , sp !base pointer BP = SP

push r1
push r2
push r3
push r4

ldrid [r7]x.2, r1 !ptrA 
ldrid [r7]x.3, r2 !ptrB
ldrid [r7]x.4, r3 !N

clr r4 !registar gde cuvam sumu
ldimm x.0, r0 !0 za compare
sumloop:
cmp r3, r0 !proveri dal je N = 0, odnosno gotovo sabiranje
beql exitroutine

ldrid [r1]x.0 , r0
add r4, r4, r0 !sa trenutne lokacije ptrA, akumuliraj u sumu

ldrid [r2]x.0, r0
add r4, r4, r0 !sa trenutne lokacije ptrB, akumuliraj u sumu

inc r1 !ptrA++
inc r2 !ptrB++
dec r3 !N--
ldimm x.0, r0 !resetujemo r0 zbog compare-a
jmp sumloop

exitroutine:
add r0, r4, r0 !prebacujemo sumu u r0 

pop r4 !popujemo sve lokalne promenljive u potprogramu
pop r3
pop r2
pop r1
pop r7 !popujemo old base pointer
rts !instrukcija ce popovati pc sa steka

! kraj potprograma -----------------------------------------------------------------------------


!prekidna rutine KP 1.1 -------------------------------------------------------------------------
org x.500

! pushovanje registara koji se koriste u pr. rutini
push r0

! provera semafora 
ldimm x.1,r0
and r5,r5,r0
bneq turnoff

ldmem x.F103,r0
stri [r1],r0
inc r1  !ptrA
dec r3	!cntA
beql semafor
jmp return

! postavljanje semafora
semafor:
ldimm x.1,r5
jmp return

! gasenje periferije
turnoff:
ldimm x.0,r0
stmem x.F100,r0
return:
pop r0
rti
!--------------------------------------------------------------------------------------------

!prekidna rutina dma 1.4--------------------------------------------------------------------
org x.3000          
push ra
clr ra
stmem x.f0c0, ra    ! gasimo DMA 1.4 kontroler upisom nule
ldimm x.1, rb       ! setujemo semafor
pop ra
rti




!prekidna rutina kp 1.2 -------------------------------------------------------------
org x.1000              
push r6

ldimm x.1, r6
and r5, r5, r6
bneq turnoff12

! ako nismo završili, šaljemo sledeci element niza A
ldrid [r1]x.0, r6   ! ucitavamo podatak sa ptrA
stmem x.f143, r6    ! upisujemo u Data registar KP 1.2
inc r1              ! ptrA++
dec rf              ! cntA--
beql semafor12 	  !ako je cntA = 0, postavljamo semafor, u suprotnom, izlazimo iz rutine
jmp exit12

semafor12: 
ldimm x.1, r5
jmp exit12

turnoff12:
clr r6
stmem x.f140, r6    ! gasimo periferiju

exit12:
pop r6
rti
!-------------------------------------------------------------------------------------
!prekidna rutina za dma 1.2-----------------------------------------------------------
org x.2500
push ra
ldimm x.1, r9 !setovanje semafora
clr ra
stmem x.f040, ra    !gasenje periferije     
pop ra
rti








