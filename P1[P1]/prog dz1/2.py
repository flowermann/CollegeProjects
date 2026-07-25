def tezine():
    unos= input().split()
    tezina=[]
    try:
        for t in unos:
            broj = int(t)
            if broj<1 or broj>5:
                return None
            
            else:
                tezina.append(broj)
    except: 
        return 
    return tezina


def sobe():
    lista_soba = []
    while True:
        soba = input().split(",")
        try:
            naziv = str(soba[0])
            broj_igraca = str(soba[1])
            tezina = int(soba[2])
            soba = [naziv, broj_igraca, tezina]
            lista_soba.append(soba)
        except:
            break
    return lista_soba

def konvertuj_broj_igraca(sobe):
    pomocna_lista = sobe.copy()
    for soba in sobe:
        granice = [int(granica) for granica in soba[1].split("-")]
        pomocna_lista[sobe.index(soba)][1] = granice
    return pomocna_lista

def dovoljan_broj_igraca(novi_format_soba,lista_tezina):
    sobe_sa_dovoljno_igraca = []
    broj_igraca = len(lista_tezina)
    for soba in novi_format_soba:
        if broj_igraca >= soba[1][0] and broj_igraca <= soba[1][1]:
            sobe_sa_dovoljno_igraca.append(soba)
        else:
            pass
    return sobe_sa_dovoljno_igraca

def dovoljna_tezina(sobe_sa_dovoljno_igraca, lista_tezina):
    sobe_sa_dobrim_tezinama = []
    for soba in sobe_sa_dovoljno_igraca:
        flag = True
        for tezina in lista_tezina:
            if soba[2] < tezina:
                flag = False
        if flag == True:
            sobe_sa_dobrim_tezinama.append(soba)
        else:
            pass
    return sobe_sa_dobrim_tezinama

def sortiraj_leksikografski(sobe_sa_dobrim_tezinama):
    imena_soba = []
    for soba in sobe_sa_dobrim_tezinama:
        imena_soba.append(soba[0])
    return sorted(imena_soba)
    
def ispis(imena_soba):
    for ime in imena_soba:
        print(ime)


# dohvatamo tezine 
lista_tezina = tezine()


if lista_tezina is None:
    pass
else:
    # dohvatamo sobe
    lista_soba = sobe()
    if lista_soba is None:
        pass
    else:
        # konvertovanje broja igraca u listu
        novi_format_soba = konvertuj_broj_igraca(lista_soba)
        # obrada po broju igraca 
        sobe_sa_dovoljno_igraca = dovoljan_broj_igraca(novi_format_soba, lista_tezina)
        # obrada po tezinama
        sobe_sa_dobrim_tezinama = dovoljna_tezina(sobe_sa_dovoljno_igraca, lista_tezina)
        # sortiranje leksikografski
        sortirane_sobe = sortiraj_leksikografski(sobe_sa_dobrim_tezinama)
        # ispis
        ispis(sortirane_sobe)