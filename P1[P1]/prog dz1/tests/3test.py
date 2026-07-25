vrednosti= [2,3,4,5,6,7,8,9,"T","J","Q","K","A"]
znakovi= ["S", "H", "D", "C"]

def talon():
    pet_karata = input()
    talon = pet_karata.split()
    if len(talon)!=5:
        return None
    else:
        pass
    return talon

def karte_igraca():
    igraci = []
    while True:
        try:
            karte = input()
            if karte == '':
                break
            ruka = karte.split(",")
            if len(ruka)!=2:
                return None
            else:
                igraci.append(ruka)
        except:
            return
    return igraci 


def najcesce_vrednosti(igraci):
    
    for broj in vrednosti:
        brojac = 0
        maksimum = 0
        najcesca_vrednost = ''
        for element in igraci:
           if element==broj:
               brojac += 1
        if brojac>maksimum:
           maksimum=brojac
           najcesca_vrednost=broj
    return maksimum,najcesca_vrednost




#ispisuje talon od pet karata
talon_karata=talon()
#ispisuje karte ("ruke") svih igraca
igraci=karte_igraca()
najcesce_karte= najcesce_vrednosti(igraci)
print(najcesce_karte)