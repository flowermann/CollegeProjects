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


def najcesce_vrednosti(igraci, talon_karata):
    # finalna lista, broj ponavljanja + koja je vrednost
    lista_najcescih_vrednosti = []
    # iteriramo po svim igracima
    for igrac in igraci:
        # pomocne promenljiva za nalazenje najcesce vrednosti
        najcesca_vr = -1
        kljuc_najcesce_vrednosti = '2'
        # spajamo sve karte u jednu listu
        sve_karte_po_igracu = igrac + talon_karata
        # recnik nam pomaze da vidimo kolko se koja karta pojavila puta
        recnik_vrednosti = {
            '2':0, '3':0, '4':0, '5':0, '6':0,
            '7':0, '8':0, '9':0, 'T':0, 'J':0,
            'Q':0, 'K':0, 'A':0
        }
        # updatujemo recnik
        for karta in sve_karte_po_igracu:
            trenutna_vrednost = karta[0]
            recnik_vrednosti[trenutna_vrednost] += 1
        # klasicno trazenje max vrednosti odnosno najcesce karte
        for kljuc, vrednost in recnik_vrednosti.items():
            if vrednost > najcesca_vr:
                # ako se javlja cesce nego dosadasnji max, zameni vrednost za maks
                najcesca_vr = vrednost
                kljuc_najcesce_vrednosti = kljuc
        # sada smo zavrsili sa trenutnim igracom pa mozemo da dodamo vrednosti u listu i krenemo ispocetka
        lista_najcescih_vrednosti.append([najcesca_vr, kljuc_najcesce_vrednosti])
    return lista_najcescih_vrednosti

def pronadji_flush(igraci, talon_karata):
    svi_flushovi = []
    for igrac in igraci:
        sve_karte_po_igracu = igrac + talon_karata
        znakovi = {"S": 0, "H": 0 , "D": 0 , "C": 0}
        
        for karta in sve_karte_po_igracu:
            trenutni_znak = karta[1]
            znakovi[trenutni_znak] += 1
        
        nasao_flush = False
        for znak, broj in znakovi.items():
            if broj >= 5:
                svi_flushovi.append(znak)
                nasao_flush = True
                break
        
        if not nasao_flush:
            svi_flushovi.append("")
    
    return svi_flushovi

                    

def ispis(lista_najcescih_vrednosti, lista_flushova):
    for i in range(len(lista_najcescih_vrednosti)):
        vrednost = lista_najcescih_vrednosti[i]
        flush = lista_flushova[i]
        if flush != "":
            print(f"{vrednost[0]}-{vrednost[1]} {flush}")
        else:
            print(f"{vrednost[0]}-{vrednost[1]}")

#ispisuje talon od pet karata
talon_karata=talon()
if talon_karata is None:
    pass
else:
    #ispisuje karte svih igraca
    igraci=karte_igraca()
    if igraci is None:
        pass
    else:
        # nalazi karte koje se najcesce javljaju
        lista_najcescih_vrednosti = najcesce_vrednosti(igraci, talon_karata)
        # nalazi za svakog igraca da li ima flush
        lista_flushova = pronadji_flush(igraci, talon_karata)
        # ispisuje 
        ispis(lista_najcescih_vrednosti,lista_flushova)

